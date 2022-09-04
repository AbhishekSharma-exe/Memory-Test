package com.example.memorytest.models

import com.example.memorytest.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize, private val customImages: List<String>?) {


    val cards: List<MemoryCard>
    var numPairFound = 0

    private var numCardsFlips = 0

    private var indexOfSingleSelectedCard: Int? = null

    init {
        if (customImages == null) {
            val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages = (chosenImages + chosenImages).shuffled()

            cards = randomizedImages.map { MemoryCard(it) }

        }
        else{
            val randomizedImages = (customImages+customImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it.hashCode(),it) }
        }
    }

    fun flipCard(position: Int) :Boolean{
        numCardsFlips++
        var foundMatch =false
        val card: MemoryCard = cards[position]
        // Three cases:
        // 0 cards previously flipped = restore cards and flip over selected cards
        // 1 cards previously flipped = flip the selected cards and check if it matches
        // 2 cards previously flipped = restore cards and flip over selected cards

        if (indexOfSingleSelectedCard == null) {
            // 0 and 2 case
            restoreCards()
            indexOfSingleSelectedCard = position
        }
        else{
            //case 1
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!,position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int):Boolean {
        if(cards[position1].identifier != cards[position2].identifier){
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairFound++
        return true
    }


    private fun restoreCards() {
        for (card :MemoryCard in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun hasWonGame(): Boolean {
        return numPairFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardsFlips/2
    }
}