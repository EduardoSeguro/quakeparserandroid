package com.eduardoseguro.quakeparserapp.model

import com.eduardoseguro.quakeparserapp.utils.DeathTypes
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GameReport(
    @SerializedName("game_id") val id: Int,
    @SerializedName("total_kills") val totalKills: Int,
    @SerializedName("players") val players: MutableList<String>,
    @SerializedName("kills") val kills: Map<String, Int>,
    @SerializedName("kills_by_means") val killsByMeans: Map<DeathTypes, Int>,
    var invalidGame: Boolean = false,
    val invalidReasons: MutableSet<String> = mutableSetOf()
) : Serializable

val gameReportPreview = GameReport(
    id = 1,
    totalKills = 15,
    players = mutableListOf("Zezinho", "Luisinho", "Huguinho"),
    kills = mutableMapOf("Zezinho" to 7, "Luisinho" to 4, "Huguinho" to 2),
    killsByMeans = mutableMapOf(DeathTypes.MOD_CHAINGUN to 9, DeathTypes.MOD_GRENADE to 4, DeathTypes.MOD_TRIGGER_HURT to 2),
    invalidGame = false,
    invalidReasons = mutableSetOf()
)