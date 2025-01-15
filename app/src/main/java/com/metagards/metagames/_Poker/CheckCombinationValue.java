package com.metagards.metagames._Poker;

import android.content.Context;

import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.model.CardModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CheckCombinationValue {
    static CheckCombinationValue mInstence;
    Context context;

    public static final int ROYAL_FLUSH = 1;
    public static final int STRAIGHT_FLUSH = 2;
    public static final int FOUR_OF_A_KIND = 3;
    public static final int FULL_HOUSE = 4;
    public static final int FLUSH = 5;
    public static final int STRAIGHT = 6;
    public static final int THREE_OF_KIND = 7;
    public static final int TWO_PAIR = 8;
    public static final int PAIR = 9;
    public static final int HIGH_CARD = 10;

    String[] rule_names = {
            "ROYAL FLUSH",
            "STRAIGHT FLUSH",
            "FOUR_OF A KIND",
            "FULL HOUSE",
            "FLUSH",
            "STRAIGHT",
            "THREE OF KIND",
            "TWO PAIR",
            "PAIR",
            "HIGH CARD"
    };

    CardModel card1;
    CardModel card2;
    CardModel card3;
    CardModel card4;
    CardModel card5;

    public String getCombinationNames(int rulevalue){
        --rulevalue;
        return rule_names[rulevalue];
    }

    public static synchronized CheckCombinationValue getInstance(){
        if(mInstence == null)
            mInstence = new CheckCombinationValue();

        return mInstence;
    }

    public CheckCombinationValue init(Context context){
        this.context=context;
        return this;
    }

    List<CardModel> usersCardList;
    HashMap<String,List<CardModel>> data;
    public int getGroupCombinationValue(List<CardModel> usersCardList){
        data = new HashMap<>();
        int group_value = HIGH_CARD;
        this.usersCardList =usersCardList;
        for (int i = 0; i < usersCardList.size() ; i++) {
            CardModel cardModel = usersCardList.get(i);

            if(i == 0)
            {
                card1 = cardModel;
            }else if(i==1)
            {
                card2 = cardModel;
            }else if(i==2)
            {
                card3 = cardModel;

            }else if(i==3)
            {
                card4 = cardModel;

            }else if(i==4)
            {
                card5 = cardModel;
            }
        }

        if(isColorMatch())
        {
            if(straightCard() && isRoyalCards)
            {
                data.put(getCombinationNames(ROYAL_FLUSH), usersCardList);
                group_value = ROYAL_FLUSH;
            }
            else if(isColorMatch() && straightCard())
            {
                data.put(getCombinationNames(STRAIGHT_FLUSH), usersCardList);
                group_value = STRAIGHT_FLUSH;
            }
            else if(isColorMatch())
            {
                data.put(getCombinationNames(FLUSH), usersCardList);
                group_value = FLUSH;
            }
        }
        else if(straightCard())
        {
            data.put(getCombinationNames(STRAIGHT), usersCardList);
            group_value = STRAIGHT;
        }
        else if(isFourofKind()) {
            Functions.LOGD("CheckCombinationValue","matchKindCardCount : "+matchKindCardCount);

            if(matchKindCardCount == 1)
            {
                data.put(getCombinationNames(PAIR), usersCardList);
                group_value = PAIR;
            }
            else if(matchKindCardCount == 2)
            {
                data.put(getCombinationNames(TWO_PAIR), usersCardList);
                group_value = TWO_PAIR;
            }
            else if(matchKindCardCount == 3)
            {
                data.put(getCombinationNames(THREE_OF_KIND), usersCardList);
                group_value = THREE_OF_KIND;
            }
            else if(matchKindCardCount == 4)
            {
                data.put(getCombinationNames(FULL_HOUSE), usersCardList);
                group_value = FULL_HOUSE;
            }
            else if(matchKindCardCount == 6)
            {
                data.put(getCombinationNames(FOUR_OF_A_KIND), usersCardList);
                group_value = FOUR_OF_A_KIND;
            }
        }

        usersCardList.get(0).setPockerGroup_value(group_value);

        return group_value;
    }

    private HashMap<String,List<CardModel>> cardList(){
        return data;
    }

    private boolean isColorMatch() {
        String CardColorArray[] = {card1.getCardColor(),card2.getCardColor()
                ,card3.getCardColor(),card4.getCardColor(),card5.getCardColor()};

        boolean isColorMatch = false;
        outerloop:
        for (int i = 0; i < CardColorArray.length; i++) {
            String cardColorPrevious = CardColorArray[i];
            cardColorPrevious = !Functions.isStringValid(cardColorPrevious) ? CardColorArray[0] : cardColorPrevious;
            for (int j = i+1; j < CardColorArray.length; j++) {
                String cardColorNext = CardColorArray[j];
                cardColorNext = !Functions.isStringValid(cardColorNext) ? CardColorArray[0] : cardColorNext;
                // compare list.get(i) and list.get(j)

                if((cardColorPrevious.equalsIgnoreCase(cardColorNext)
                        && (Functions.isStringValid(cardColorPrevious) || Functions.isStringValid(cardColorNext)))) {
                    isColorMatch = true;
                }
                else {
                    isColorMatch = false;
                    break outerloop;
                }
            }
        }
        return isColorMatch;
    }

    int matchKindCardCount = 0;
    private boolean isFourofKind()
    {
        boolean isFourofKind = false;

        int CardNumberArray[] = {card1.getCardNumber(),card2.getCardNumber()
                ,card3.getCardNumber(),card4.getCardNumber(),card5.getCardNumber()};

        matchKindCardCount = 0;

        outerloop:
        for (int i = 0; i < CardNumberArray.length; i++) {
            int cardNumberPrevious = CardNumberArray[i];
            cardNumberPrevious = cardNumberPrevious == 0 ? CardNumberArray[0] : cardNumberPrevious;
            for (int j = i+1; j < CardNumberArray.length; j++) {
                int cardColorNext = CardNumberArray[j];
                cardColorNext = cardColorNext == 0 ? CardNumberArray[0] : cardColorNext;
                // compare list.get(i) and list.get(j)

                if((cardNumberPrevious == cardColorNext)) {
                    ++matchKindCardCount;
                    isFourofKind = true;
//                    break outerloop;
                }
                else {
//                    if(isFourofKind)
//                        isFourofKind = false;
                }
            }
        }

        return isFourofKind;
    }

    boolean isRoyalCards = false;
    private boolean straightCard(){
        boolean isStraight = false;

        Integer cardNumberArray[] = {card1.getCardNumber(),card2.getCardNumber()
                ,card3.getCardNumber(),card4.getCardNumber(),card5.getCardNumber()};

        if(checkSequence(cardNumberArray,0))
        {
            isStraight = true;
        }

        return isStraight;
    }

    private boolean checkSequence(Integer $arr[],int $joker_count){
        boolean $sequence = true;
        int $total_card = $arr.length;
        Arrays.sort($arr);

        isRoyalCards = $arr[0] >= 10;

        for (int j = 0; j < $arr.length - 1; j++) {

            int $val = $arr[j];

            if($val!=0 && $total_card>(j+1))
            {
                int card_gap = $arr[j + 1] - ($val+1);
                if(card_gap >= 0 && (card_gap == 0 || card_gap <= $joker_count))
                {
                    $joker_count = $joker_count - card_gap;
                }
                else {
                    $sequence = false;
                    break;
                }
            }


        }

        return $sequence;
    }

}
