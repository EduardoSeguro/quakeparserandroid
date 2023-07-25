package com.eduardoseguro.quakeparserapp.model

import com.eduardoseguro.quakeparserapp.utils.DeathTypes

class Game(
    val id: Int,
    var totalKills: Int = 0,
    val players: MutableList<Player> = mutableListOf(),
    var kills: MutableMap<String, Int> = mutableMapOf(),
    var killsByTypes: MutableMap<DeathTypes, Int> = mutableMapOf(),
    var gameRunning: Boolean = false,
    var invalidGame: Boolean = false,
    val invalidReasons: MutableSet<String> = mutableSetOf()
)