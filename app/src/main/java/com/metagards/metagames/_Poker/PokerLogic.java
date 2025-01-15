package com.metagards.metagames._Poker;

import android.content.Context;

import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.model.CardModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PokerLogic {

    List<List<CardModel>> combinationArray = new ArrayList<>();
    List<List<CardModel>> rulesValueArray = new ArrayList<>();
    static PokerLogic mInstence;
    Context context;

    public static synchronized PokerLogic getInstance(){
        if(mInstence == null)
            mInstence = new PokerLogic();

        return mInstence;
    }

    public PokerLogic init(Context context){
     this.context=context;
     return this;
    }

    CardModel card1;
    CardModel card2;
    CardModel card3;
    CardModel card4;
    CardModel card5;
    CardModel card6;
    CardModel card7;
    List<CardModel> usersCardList;
    public int getPockerRanking(List<CardModel> usersCardList) {
        this.usersCardList = usersCardList;
        int pockerRanking = 0;

        List<Integer> cardvalue = new ArrayList<>();
        for (int i = 0; i < usersCardList.size() ; i++) {
            CardModel cardModel = usersCardList.get(i);

            if(i == 0)
            {
                card1 = cardModel;
                cardvalue.add(i);
            }else if(i==1)
            {
                card2 = cardModel;
                cardvalue.add(i);
            }else if(i==2)
            {
                card3 = cardModel;
                cardvalue.add(i);
            }else if(i==3)
            {
                card4 = cardModel;
                cardvalue.add(i);
            }else if(i==4)
            {
                card5 = cardModel;
                cardvalue.add(i);
            }else if(i==5)
            {
                card6 = cardModel;
                cardvalue.add(i);
            }else if(i==6)
            {
                card7 = cardModel;
                cardvalue.add(i);
            }
        }



        int r = 5;
        int n = cardvalue.size();
        combinationArray.clear();
        rulesValueArray.clear();
        printCombination(cardvalue, n, r);
        List<Integer> allCombinationValue = new ArrayList<>();
        for (int i = 0; i < combinationArray.size() ; i++) {
            List<CardModel> cardModelList = combinationArray.get(i);
            int groupValue = CheckCombinationValue.getInstance()
                    .init(context).getGroupCombinationValue(cardModelList);
//            cardModelList.get(0).setPockerGroup_value(groupValue);
            allCombinationValue.add(groupValue);
        }

        Collections.sort(combinationArray, new Comparator<List<CardModel>>() {
            @Override
            public int compare(List<CardModel> cardModels, List<CardModel> t1) {

                CardModel cardModelPre = cardModels.get(0);
                CardModel cardModelNex = t1.get(0);

                return cardModelPre.getPockerGroup_value() - cardModelNex.getPockerGroup_value();
            }
        });
        try {
            pockerRanking = combinationArray.get(0).get(0).getPockerGroup_value();

            for (List<CardModel> ruleArray: combinationArray) {
                if(pockerRanking == ruleArray.get(0).getPockerGroup_value())
                {
                    rulesValueArray.add(ruleArray);
                }
                else {
                    break;
                }
            }

            if(rulesValueArray.size() > 1)
            {
                Collections.sort(rulesValueArray, new Comparator<List<CardModel>>() {
                    @Override
                    public int compare(List<CardModel> preList, List<CardModel> nexList) {
                        CardModel preModel = preList.get(0);
                        CardModel nexModel = nexList.get(0);
                        return preModel.getGroupPoints() - nexModel.getGroupPoints();
                    }
                });
                Collections.reverse(rulesValueArray);
                for (List<CardModel> cardList: rulesValueArray) {
                        Functions.LOGD("PokerLogic","grouppoint : "+cardList.get(0).getGroupPoints());
                }
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }


        return pockerRanking;
    }

    public List<CardModel> userCardList (){
        return rulesValueArray != null && rulesValueArray.size() > 0 ? rulesValueArray.get(0) : combinationArray != null ? combinationArray.get(0) : null;
    }

    /* arr[] ---> Input Array
	data[] ---> Temporary array to store current combination
	start & end ---> Starting and Ending indexes in arr[]
	index ---> Current index in data[]
	r ---> Size of a combination to be printed */
    void combinationUtil(List<Integer> arr, int data[], int start,
                                int end, int index, int r)
    {
        // Current combination is ready to be printed, print it
        if (index == r)
        {
            List<CardModel> cardModel = new ArrayList<>();
//            CardModel comCard1 = getCardsData(card1);
//            CardModel comCard2 = getCardsData(card2);
//            cardModel.add(comCard1);
//            cardModel.add(comCard2);
            int cardTotal = 0;
            for (int j=0; j<r; j++)
            {
                CardModel modelCard = getCardsData(usersCardList.get(data[j]));
                cardTotal += modelCard.getCardNumber();
                cardModel.add(modelCard);
            }//                System.out.print(data[j]+" ");
            System.out.println("");
            Collections.sort(cardModel,new Comparator<CardModel>() {
                @Override
                public int compare(CardModel cardModelPre, CardModel cardModelNex) {

                    return cardModelPre.getCardNumber() - cardModelNex.getCardNumber();
                }
            });
            cardModel.get(0).setGroup_points(""+cardTotal);
            combinationArray.add(cardModel);
            for (List<CardModel> combination: combinationArray) {
                Functions.LOGD("PockerLogic","combinationArray : "+combination.size());
                for (CardModel model: combination) {
                    Functions.LOGD("PockerLogic","model : "+model.toString());
                }
            }
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr.get(i);
            combinationUtil(arr, data, i+1, end, index+1, r);
        }
    }

    private CardModel getCardsData(CardModel card1) {
        CardModel model = new CardModel();
        model.setId(card1.getCard());
        model.setCard(card1.getCard());
        model.setCardNumber(card1.getCardNumber());
        model.setCardColor(card1.getCardColor());
        return model;
    }

    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    void printCombination(List<Integer> arr, int n, int r)
    {
        // A temporary array to store all combination one by one
        int data[]=new int[r];

        // Print all combination using temporary array 'data[]'
        combinationUtil(arr, data, 0, n-1, 0, r);
    }

}
