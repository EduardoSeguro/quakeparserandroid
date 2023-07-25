package com.eduardoseguro.quakeparserapp.usecase

import android.content.ContentResolver
import android.net.Uri
import com.eduardoseguro.quakeparserapp.model.Game
import com.eduardoseguro.quakeparserapp.model.GameReport
import com.eduardoseguro.quakeparserapp.model.Player
import com.eduardoseguro.quakeparserapp.utils.Constants
import com.eduardoseguro.quakeparserapp.utils.DeathTypes

class ParseLogFileUseCase(private val contentResolver: ContentResolver) {

    private val gameList = mutableListOf<Game>()
    private val gameReportList = mutableListOf<GameReport>()
    private var currentGame = Game(id = 0)

    suspend fun loadLog(file: Uri?): List<String> {
        if (file != null) {
            val inputStream = contentResolver.openInputStream(file)
            val logFile = inputStream?.readBytes()?.toString(Charsets.UTF_8)
            inputStream?.close()
            if (logFile == null || !logFile.contains(Constants.INIT_GAME)) {
                return listOf(Constants.INVALID_LOG_FILE)
            }
            return logFile.lines()
        } else {
            return listOf(Constants.NO_FILE_SELECTED)
        }
    }

    suspend fun parseLogData(lines: List<String>): List<GameReport> {
        clearGameData()
        readLogLines(lines)
        return gameReportList
    }

    private fun clearGameData() {
        gameList.clear()
        gameReportList.clear()
        currentGame = Game(id = 0)
    }

    private fun readLogLines(lines: List<String>) {
        lines.forEach { line ->
            if (checkInitGame(line)) {
                startNewGame()
            }
            if (checkShutDownGame(line)) {
                closeCurrentGame()
            }
            if (checkClientUserInfoChanged(line)) {
                updatePlayerInfo(line)
            }
            if (checkKill(line)) {
                updateKillCount(line)
            }
            if (checkClientDisconnect(line)) {
                disconnectPlayer(line)
            }
        }
    }

    private fun startNewGame() {
        if (currentGame.gameRunning) {
            closeCurrentGame()
        }
        currentGame = Game(id = gameList.size)
        currentGame.gameRunning = true
    }

    private fun closeCurrentGame() {
        currentGame.gameRunning = false
        updateGameKillData()
        gameList.add(currentGame)
        generateReport(currentGame)
    }

    private fun disconnectPlayers(players: List<Player>) {
        players.forEach { it.isConnected = false }
    }

    private fun disconnectPlayer(line: String) {
        val splittedLine = line.split(":")
        val playerId = splittedLine.last().trim().toInt()
        val player = currentGame.players.firstOrNull() { it.id == playerId }
        if (player == null) {
            currentGame.invalidGame = true
            currentGame.invalidReasons.add(Constants.INVALID_PLAYER_DISCONNECT)
        } else {
            player.isConnected = false
        }
    }

    private fun updatePlayerInfo(line: String) {
        val splittedLine = line.split(Constants.CLIENT_USER_INFO_CHANGED)[1].split("\\")
        val id = splittedLine[0].removeSuffix("n").trim().toInt()
        val name = splittedLine[1]

        val players = currentGame.players.filter { it.id == id }
        disconnectPlayers(players)

        val player = currentGame.players.firstOrNull { it.name == name }
        if (player == null) {
            currentGame.players.add(Player(id = id, name = name, isConnected = true))
        } else {
            player.id = id
            player.isConnected = true
        }
    }

    private fun updateKillCount(line: String) {
        val splittedLine = line.split(Constants.KILL)[1].trim().split(":")[0].split(" ")
        val killerId = splittedLine[0].toInt()
        val deadId = splittedLine[1].toInt()
        val weaponId = splittedLine[2].toInt()
        val deathType = DeathTypes.values()[weaponId]

        if (checkValidPlayers(killerId, deadId)) {
            currentGame.totalKills++
            if (killerId == Constants.WORLD) {
                currentGame.players.first { it.id == deadId && it.isConnected }.suicideCount++
                currentGame.players.first { it.id == deadId && it.isConnected }.deaths++
            } else {
                currentGame.players.first { it.id == killerId && it.isConnected }.killCount++
                currentGame.players.first { it.id == deadId && it.isConnected }.deaths++
            }
            currentGame.killsByTypes[deathType] = currentGame.killsByTypes[deathType]?.plus(1) ?: 1
        } else {
            currentGame.invalidGame = true
            currentGame.invalidReasons.add(Constants.INVALID_PLAYER_KILL)
        }
    }

    private fun checkValidPlayers(killerId: Int, deadId: Int): Boolean {
        val player1 = currentGame.players.firstOrNull { it.id == killerId && it.isConnected }
        val player2 = currentGame.players.firstOrNull { it.id == deadId && it.isConnected }
        return (player1 != null || killerId == Constants.WORLD) && player2 != null
    }

    private fun updateGameKillData() {
        currentGame.players.forEach { player ->
            currentGame.kills[player.name] = player.getKillTotal()
        }
    }

    private fun generateReport(game: Game) {
        val playersList = mutableListOf<String>()
        val kills = mutableMapOf<String, Int>()
        game.players.forEach {
            playersList.add(it.name)
            kills[it.name] = it.getKillTotal()
        }

        val gameReport = GameReport(
            id = game.id,
            totalKills = game.totalKills,
            players = playersList,
            kills = kills.toList().sortedByDescending { (_, value) -> value }.toMap(),
            killsByMeans = game.killsByTypes.toList().sortedByDescending { (_, value) -> value }.toMap(),
            invalidGame = game.invalidGame,
            invalidReasons = game.invalidReasons
        )
        gameReportList.add(gameReport)
    }

    private fun checkInitGame(line: String) = line.contains(Constants.INIT_GAME)
    private fun checkShutDownGame(line: String) = line.contains(Constants.SHUTDOWN_GAME)
    private fun checkClientDisconnect(line: String) = line.contains(Constants.CLIENT_DISCONNECT)
    private fun checkClientUserInfoChanged(line: String) = line.contains(Constants.CLIENT_USER_INFO_CHANGED)
    private fun checkKill(line: String) = line.contains(Constants.KILL)
}