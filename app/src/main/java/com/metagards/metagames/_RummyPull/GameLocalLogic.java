package com.metagards.metagames._RummyPull;

import static com.metagards.metagames._Rummy.RummPoint.removeLastChars;

import android.content.Context;

import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.model.CardModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameLocalLogic {

    Context context;
    Callback callback;
    String group_params = "";
    String joker_card = "";

    private volatile static GameLocalLogic mInstance;

    String INVALID = "Invalid";

    String IMPURE_SEQUENCE = "Impure sequence";
    String PURE_SEQUENCE = "Pure Sequence";
    String SET = "set";

    String DECLARE_BACK = "";
    String JOKER_CARD = "JK";
    String MAIN_JOKER_CARD = "JKR1";
    String MAIN_JOKER_CARD_2 = "JKR2";
    String DUMMY_CARD = "backside_card";

    int IMPURE_SEQUENCE_VALUE = 4;
    int PURE_SEQUENCE_VALUE = 5;
    int SET_VALUE = 6;


    public static GameLocalLogic getInstance(Context context) {
        if (null == mInstance) {
            synchronized (GameLocalLogic.class) {
                if (null == mInstance) {
                    mInstance = new GameLocalLogic();
                }
            }
        }

        if(mInstance != null)
            mInstance.init(context);

        return mInstance;
    }

    /**
     * initialization of context, use only first time later it will use this again and again
     *
     * @param context app context: first time
     */
    public void init(Context context) {
        try {

            if (context != null) {
                this.context = context;
            }

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void setJokerCard(String joker_card){
        this.joker_card = joker_card;
    }

    public ArrayList<CardModel> CardValue(ArrayList<CardModel> arrayList) {

        int $rule = 0;
        int $value = 0;
        int group_size = arrayList.size();

        arrayList.get(0).group_value_params = group_params;
        arrayList.get(0).group_value_response = ""+$rule;

        arrayList.get(0).group_value = ""+getGroupStatus($rule);
        arrayList.get(0).group_points = ""+getGroupPoints(arrayList);
        arrayList.get(0).value_grp = $rule;

        arrayList =  setValueList(arrayList,$rule,arrayList.get(0).group_points,arrayList.get(0).group_value);

        if (group_size <= 2) {

            Collections.sort(arrayList, new Comparator<CardModel>() {
                @Override
                public int compare(CardModel o1, CardModel o2) {

                    int cardValue1 = Integer.parseInt(convertCardIntoNumber(o1.Image));
                    int cardValue2 = Integer.parseInt(convertCardIntoNumber(o2.Image));

                    return cardValue1 - cardValue2;
                }
            });

            Collections.reverse(arrayList);

            return arrayList;
        }

        if(joker_card.substring(joker_card.length() - 1).equalsIgnoreCase("_"))
        {
            joker_card = removeLastChars(joker_card,1);
        }

        String $joker_num = "" + joker_card.substring(2);

        String $set = "";
        String $color_group = "";

        String $card1_color = "";
        String $card2_color = "";
        String $card3_color = "";
        String $card4_color = "";
        String $card5_color = "";
        String $card6_color = "";
        String $card7_color = "";
        String $card8_color = "";
        String $card9_color = "";
        String $card10_color = "";

        String $card1_color_set = "";
        String $card2_color_set = "";
        String $card3_color_set = "";
        String $card4_color_set = "";
        String $card5_color_set = "";
        String $card6_color_set = "";
        String $card7_color_set = "";
        String $card8_color_set = "";
        String $card9_color_set = "";
        String $card10_color_set = "";

        String $card1_num = "-1";
        String $card2_num = "-1";
        String $card3_num = "-1";
        String $card4_num = "-1";
        String $card5_num = "-1";
        String $card6_num = "-1";
        String $card7_num = "-1";
        String $card8_num = "-1";
        String $card9_num = "-1";
        String $card10_num = "-1";

        String $card1_num_set = "-1";
        String $card2_num_set = "-1";
        String $card3_num_set = "-1";
        String $card4_num_set = "-1";
        String $card5_num_set = "-1";
        String $card6_num_set = "-1";
        String $card7_num_set = "-1";
        String $card8_num_set = "-1";
        String $card9_num_set = "-1";
        String $card10_num_set = "-1";

        for (int i = 0; i < group_size; i++) {
            CardModel cards_model = arrayList.get(i);

            String _cards_name = cards_model.Image;

            if(_cards_name.equalsIgnoreCase(DUMMY_CARD))
                continue;

            if(_cards_name.equalsIgnoreCase(MAIN_JOKER_CARD) || _cards_name.equalsIgnoreCase(MAIN_JOKER_CARD_2))
                _cards_name = joker_card;

            cards_model.setCardColor(getColorCode(_cards_name));
            cards_model.setCardNumber(Integer.parseInt(ConvertSpecialtoNumber(getCardNumber(_cards_name))));


            if (i == 0) {

                $card1_color = getColorCode(_cards_name);
                $card1_num = getCardNumber(_cards_name);
                $card1_color_set = $card1_color;
                $card1_num_set = $card1_num;
                card1 = cards_model;
            }

            if (i == 1) {

                $card2_color = getColorCode(_cards_name);
                $card2_num = getCardNumber(_cards_name);
                $card2_color_set = $card2_color;
                $card2_num_set = $card2_num;
                card2 = cards_model;

            }

            if (i == 2) {

                $card3_color = getColorCode(_cards_name);
                $card3_num = getCardNumber(_cards_name);
                $card3_color_set = $card3_color;
                $card3_num_set = $card3_num;
                card3 = cards_model;

            }

            if (i == 3) {

                $card4_color = getColorCode(_cards_name);

                $card4_num = getCardNumber(_cards_name);

                $card4_color_set = $card4_color;
                $card4_num_set = $card4_num;
                card4 = cards_model;


            }

            if (i == 4) {

                $card5_color = getColorCode(_cards_name);

                $card5_num = getCardNumber(_cards_name);

                $card5_color_set = $card5_color;
                $card5_num_set = $card5_num;
                card5 = cards_model;

            }

            if (i == 5) {

                $card6_color = getColorCode(_cards_name);

                $card6_num = getCardNumber(_cards_name);

                $card6_color_set = $card6_color;
                $card6_num_set = $card6_num;
                card6 = cards_model;

            }

            if (i == 6) {

                $card7_color = getColorCode(_cards_name);

                $card7_num = getCardNumber(_cards_name);

                $card7_color_set = $card7_color;
                $card7_num_set = $card7_num;
                card7 = cards_model;

            }

            if (i == 7) {

                $card8_color = getColorCode(_cards_name);

                $card8_num = getCardNumber(_cards_name);

                $card8_color_set = $card8_color;
                $card8_num_set = $card8_num;
                card8 = cards_model;

            }

            if (i == 8) {

                $card9_color = getColorCode(_cards_name);

                $card9_num = getCardNumber(_cards_name);

                $card9_color_set = $card9_color;
                $card9_num_set = $card9_num;
                card9 = cards_model;

            }

            if (i == 9) {

                $card10_color = getColorCode(_cards_name);

                $card10_num = getCardNumber(_cards_name);

                $card10_color_set = $card10_color;
                $card10_num_set = $card10_num;
                card10 = cards_model;

            }

        }

        if(isColorMatch())
        {
            if(straightCard())
            {
                $rule = PURE_SEQUENCE_VALUE;
                arrayList.get(0).group_value_params = group_params;
                arrayList.get(0).group_value_response = ""+$rule;

                String card_group_value = getGroupStatus($rule);

                arrayList.get(0).group_points = ""+card_group_value;
                arrayList.get(0).group_value = card_group_value;

                arrayList.get(0).value_grp = $rule;

                arrayList = setValueList(arrayList,$rule,arrayList.get(0).group_points,arrayList.get(0).group_value);

                return arrayList;
            }
        }

        // Check Wild Joker And Convert To Joker Card
        if($card1_num.equalsIgnoreCase($joker_num))
        {
            $card1_color = JOKER_CARD;
            $card1_num = "0";
            $card1_num_set = $card1_num;
        }
        if($card2_num.equalsIgnoreCase($joker_num))
        {
            $card2_color = JOKER_CARD;
            $card2_num = "0";
            $card2_num_set = $card2_num;
        }
        if($card3_num.equalsIgnoreCase($joker_num))
        {
            $card3_color = JOKER_CARD;
            $card3_num = "0";
            $card3_num_set = $card3_num;
        }
        if($card4_num.equalsIgnoreCase($joker_num))
        {
            $card4_color = JOKER_CARD;
            $card4_num = "0";
            $card4_num_set = $card4_num;

        }
        if($card5_num.equalsIgnoreCase($joker_num))
        {
            $card5_color = JOKER_CARD;
            $card5_num = "0";
            $card5_num_set = $card5_num;

        }
        if($card6_num.equalsIgnoreCase($joker_num))
        {
            $card6_color = JOKER_CARD;
            $card6_num = "0";
            $card6_num_set = $card6_num;

        }
        if($card7_num.equalsIgnoreCase($joker_num))
        {
            $card7_color = JOKER_CARD;
            $card7_num = "0";
            $card7_num_set = $card7_num;

        }
        if($card8_num.equalsIgnoreCase($joker_num))
        {
            $card8_color = JOKER_CARD;
            $card8_num = "0";
            $card8_num_set = $card8_num;

        }
        if($card9_num.equalsIgnoreCase($joker_num))
        {
            $card9_color = JOKER_CARD;
            $card9_num = "0";
            $card9_num_set = $card9_num;

        }
        if($card10_num.equalsIgnoreCase($joker_num))
        {
            $card10_color = JOKER_CARD;
            $card10_num = "0";
            $card10_num_set = $card10_num;

        }
        // END Check Wild Joker And Convert To Joker Card


// Color Group Code
        if (!$card1_color.equalsIgnoreCase(JOKER_CARD)) {
            $set = $card1_num_set;
            $color_group = $card1_color;
        } else if (!$card2_color.equalsIgnoreCase(JOKER_CARD)) {
            $set = $card2_num_set;
            $color_group = $card2_color;
        } else if (!$card3_color.equalsIgnoreCase(JOKER_CARD)) {
            $set = $card3_num_set;
            $color_group = $card3_color;
        } else if (!$card4_color.equalsIgnoreCase(JOKER_CARD)) {
            $set = $card4_num_set;
            $color_group = $card4_color;
        } else if (!$card5_color.equalsIgnoreCase(JOKER_CARD)) {
            $set = $card5_num_set;
            $color_group = $card5_color;
        } else {
            $set = $card6_num_set;
            $color_group = $card6_color;
        }
        //END Color Group Cod

        // Convert Joker to Vurtual Card
        int $joker_count = 0;
        if ($card1_color.equalsIgnoreCase(JOKER_CARD)) {
            $card1_num_set = $set;
            $card1_color = $color_group;
            $card1_color_set = "";
            $joker_count++;
        }

        if ($card2_color.equalsIgnoreCase(JOKER_CARD)) {
            $card2_num_set = $set;
            $card2_color = $color_group;
            $card2_color_set = "";

            $joker_count++;
        }

        if ($card3_color.equalsIgnoreCase(JOKER_CARD)) {
            $card3_num_set = $set;
            $card3_color = $color_group;
            $card3_color_set = "";

            $joker_count++;
        }

        if ($card4_color.equalsIgnoreCase(JOKER_CARD)) {
            $card4_num_set = $set;
            $card4_color = $color_group;
            $card4_color_set = "";

            $joker_count++;
        }

        if ($card5_color.equalsIgnoreCase(JOKER_CARD)) {
            $card5_num_set = $set;
            $card5_color = $color_group;
            $card5_color_set = "";

            $joker_count++;
        }

        if ($card6_color.equalsIgnoreCase(JOKER_CARD)) {
            $card6_num_set = $set;
            $card6_color = $color_group;
            $card6_color_set = "";

            $joker_count++;
        }

        if ($card7_color.equalsIgnoreCase(JOKER_CARD)) {
            $card7_num_set = $set;
            $card7_color = $color_group;
            $card7_color_set = "";

            $joker_count++;
        }

        if ($card8_color.equalsIgnoreCase(JOKER_CARD)) {
            $card8_num_set = $set;
            $card8_color = $color_group;
            $card8_color_set = "";

            $joker_count++;
        }

        if ($card9_color.equalsIgnoreCase(JOKER_CARD)) {
            $card9_num_set = $set;
            $card9_color = $color_group;
            $card9_color_set = "";

            $joker_count++;
        }

        if ($card10_color.equalsIgnoreCase(JOKER_CARD)) {
            $card10_num_set = $set;
            $card10_color = $color_group;
            $card10_color_set = "";

            $joker_count++;
        }

        //END Convert Joker to Vurtual Card

        $card1_num_set = ConvertSpecialtoNumber($card1_num_set);
        $card2_num_set = ConvertSpecialtoNumber($card2_num_set);
        $card3_num_set = ConvertSpecialtoNumber($card3_num_set);
        $card4_num_set = ConvertSpecialtoNumber($card4_num_set);
        $card5_num_set = ConvertSpecialtoNumber($card5_num_set);
        $card6_num_set = ConvertSpecialtoNumber($card6_num_set);
        $card7_num_set = ConvertSpecialtoNumber($card7_num_set);
        $card8_num_set = ConvertSpecialtoNumber($card8_num_set);
        $card9_num_set = ConvertSpecialtoNumber($card9_num_set);
        $card10_num_set = ConvertSpecialtoNumber($card10_num_set);

        int numberOne = Integer.parseInt($card1_num_set);
        int numberTwo = Integer.parseInt($card2_num_set);
        int numberThree = Integer.parseInt($card3_num_set);
        int numberFour = Integer.parseInt($card4_num_set);
        int numberFive = Integer.parseInt($card5_num_set);
        int numberSix = Integer.parseInt($card6_num_set);
        int numberSeven = Integer.parseInt($card7_num_set);
        int numberEight = Integer.parseInt($card8_num_set);
        int numberNine = Integer.parseInt($card9_num_set);
        int numberTen = Integer.parseInt($card10_num_set);

        int CardNumberArray[] = {numberOne,numberTwo,numberThree,numberFour,numberFive,numberSix,numberSeven,numberEight,numberNine,numberTen};
        String CardColorArray[] = {$card1_color_set,$card2_color_set,$card3_color_set,$card4_color_set,$card5_color_set,$card6_color_set,$card7_color_set,$card8_color_set,$card9_color_set,$card10_color_set};

        List<Integer> numlist = new ArrayList<Integer>();
        for(int i= 0;i<CardNumberArray.length;i++)
        {
            if(CardNumberArray[i] >= 0)
            {
                numlist.add(CardNumberArray[i]);
            }

        }

        boolean isColorMatch = false;
        boolean isset = false;
        outerloop:
        for (int i = 0; i < CardColorArray.length; i++) {
            for (int j = i+1; j < CardColorArray.length; j++) {
                // compare list.get(i) and list.get(j)

                if((CardColorArray[i].equalsIgnoreCase(CardColorArray[j])
                        && (Functions.checkisStringValid(CardColorArray[i]) || Functions.checkisStringValid(CardColorArray[j])))) {
                    isColorMatch = true;
                    break outerloop;
                }
                else {
                    isColorMatch = false;
                }

            }
        }

        if(!isColorMatch)
        {
            outerloop1:
            for (int i = 0; i < numlist.size(); i++) {

                int card_number = numlist.get(i);

                for (int j = i+1; j < numlist.size(); j++) {
                    // compare list.get(i) and list.get(j)

                    int card_number1  = numlist.get(j);


                    if(card_number == card_number1) {
                        isset = true;
                    }
                    else {
                        isset = false;
                        break outerloop1;
                    }

                }
            }
        }

        int total_joker = 0;
        if(isset)
        {
            $rule = 6;
        }
        else {

            boolean $color = false;

            $color = false;

            String[] CardColorArray1 = {$card1_color,$card2_color,$card3_color,$card4_color,$card5_color,$card6_color,$card7_color,$card8_color,$card9_color,$card10_color};

            List<String> colorlist = new ArrayList<String>();
            for(int i= 0;i<CardColorArray1.length;i++)
            {
                if(Functions.checkisStringValid(CardColorArray1[i]))
                {
                    colorlist.add(CardColorArray1[i]);
                }

            }

            outerloop3:
            for (int i = 0; i < colorlist.size() ; i++) {

                String card_color = colorlist.get(i);

                for (int j = 0; j < colorlist.size(); j++) {

                    String card_color2 = colorlist.get(j);


                    if(card_color.equalsIgnoreCase(card_color2))
                    {
                        $color = true;
                    }
                    else {
                        $color = false;
                        break outerloop3;
                    }

                }

            }

//                if (($card1_color.equalsIgnoreCase($card2_color)) && ($card2_color.equalsIgnoreCase($card3_color)))
//                {
//
//                    if (Funtions.checkisStringValid($card6_color) && !$card5_color.equalsIgnoreCase($card6_color)) {
//                        return $value;
//                    } else if (Funtions.checkisStringValid($card5_color) && !$card4_color.equalsIgnoreCase($card5_color)) {
//                        return $value;
//                    } else if (Funtions.checkisStringValid($card4_color) && !$card3_color.equalsIgnoreCase($card4_color)) {
//                        return $value;
//                    }
//
//                    $color = true;
//
//                }
//                else {
//                    return $value;
//                }

            $card1_num = ConvertSpecialtoNumber($card1_num);
            $card2_num = ConvertSpecialtoNumber($card2_num);
            $card3_num = ConvertSpecialtoNumber($card3_num);
            $card4_num = ConvertSpecialtoNumber($card4_num);
            $card5_num = ConvertSpecialtoNumber($card5_num);
            $card6_num = ConvertSpecialtoNumber($card6_num);
            $card7_num = ConvertSpecialtoNumber($card7_num);
            $card8_num = ConvertSpecialtoNumber($card8_num);
            $card9_num = ConvertSpecialtoNumber($card9_num);
            $card10_num = ConvertSpecialtoNumber($card10_num);

            numberOne = Integer.parseInt($card1_num);
            numberTwo = Integer.parseInt($card2_num);
            numberThree = Integer.parseInt($card3_num);
            numberFour = Integer.parseInt($card4_num);
            numberFive = Integer.parseInt($card5_num);
            numberSix = Integer.parseInt($card6_num);
            numberSeven = Integer.parseInt($card7_num);
            numberEight = Integer.parseInt($card8_num);
            numberNine = Integer.parseInt($card9_num);
            numberTen = Integer.parseInt($card10_num);

            int CardNumberArray1[] = {numberOne,numberTwo,numberThree,numberFour,numberFive,numberSix,numberSeven,numberEight,numberNine,numberTen};

            numlist = new ArrayList<Integer>();
            for(int i= 0;i<CardNumberArray1.length;i++)
            {
                if(CardNumberArray1[i] >= 0)
                {
                    numlist.add(CardNumberArray1[i]);
                }

            }

            Integer $arr[] = numlist.toArray(new Integer[numlist.size()]);


            Arrays.sort($arr);

            boolean $sequence = false;
            int $ace_joker_count = $joker_count;
            total_joker = $joker_count;
            int $total_card = $arr.length;

            // need to made changes here of 2 joker if card ares jk,jk,3,6
            // need to made changes here of 2 joker if card ares jk,3,5
            $sequence = checkSequence($arr,$joker_count);

            if ($sequence && $color) {
                $value = $arr[0];
                $rule = ($value == 0) ? 4 : 5;
            }

            if ($rule == 0) {

                if(in_array(14,$arr))
                {
                    $arr = str_replace(14,1,$arr);
                    Arrays.sort($arr);

                    $sequence = checkSequence($arr,$ace_joker_count);
                }

                if ($sequence && $color) {
                    $value = $arr[0];
                    $rule = ($value == 0) ? 4 : 5;
                }

            }

        }

        if(!isset)
        {
            Collections.sort(arrayList, new Comparator<CardModel>() {
                @Override
                public int compare(CardModel o1, CardModel o2) {

                    int cardValue1 = Integer.parseInt(convertCardIntoNumber(o1.Image));
                    int cardValue2 = Integer.parseInt(convertCardIntoNumber(o2.Image));

                    return cardValue1 - cardValue2;
                }
            });

            Collections.reverse(arrayList);

            if($rule == IMPURE_SEQUENCE_VALUE)
            {
                int sequenceCount = 0;
                ArrayList<CardModel> impureList = new ArrayList<>();
//                for (int i = 0; i < arrayList.size(); i++) {
//                    CardModel previousmodel = arrayList.get(i);
//
//                    int previous_card = Integer.parseInt(convertCardIntoNumber(previousmodel.Image));
//
//                    if($joker_num.equals(""+previous_card) || previous_card == 0)
//                        continue;
//
//                    if(i == 0)
//                    {
//                        impureList.add(previousmodel);
//                    }
//
//                    if(total_joker <= (i+1) && arrayList.size() >= (i+1))
//                    {
//                        CardModel nextmodel = arrayList.get(i+1);
//                        int next_card = Integer.parseInt(convertCardIntoNumber(nextmodel.Image));
//
//                        if(previous_card - next_card != 1)
//                        {
//                            impureList.add(arrayList.get(0));
//                            arrayList.remove(0);
////                            Collections.swap(arrayList, 0, i+1);
//                        }
//
//                    }
//
//                }

//                Collections.reverse(arrayList);
            }
            else {

            }

        }

        int group_total_point = getGroupPoints(arrayList);

        if(group_total_point == 0)
        {
            $rule = 6;
        }

        arrayList.get(0).group_value_params = group_params;
        arrayList.get(0).group_value_response = ""+$rule;

        String card_group_value = getGroupStatus($rule);

        if(card_group_value.equals(INVALID))
        {
            arrayList.get(0).group_points = ""+getGroupPoints(arrayList);
            arrayList.get(0).group_value = ""+card_group_value;
        }
        else
        {
            arrayList.get(0).group_points = ""+card_group_value;
            arrayList.get(0).group_value = card_group_value;
        }

        arrayList.get(0).value_grp = $rule;


        arrayList = setValueList(arrayList,$rule,arrayList.get(0).group_points,arrayList.get(0).group_value);

        return arrayList;
    }

    private boolean checkSequence(Integer $arr[],int $joker_count){
        boolean $sequence = true;
        int $total_card = $arr.length;

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

    private boolean in_array(int value,Integer[] array)
    {
        boolean isAvailable = false;

        for (int i = 0; i < array.length ; i++) {

            if(array[i] == value)
            {
                isAvailable = true;
                break;
            }

        }

        return isAvailable;
    }

    private Integer[] str_replace(int value,int replace,Integer[] array)
    {

        for (int i = 0; i < array.length ; i++) {

            if(array[i] == value)
            {
                array[i] = replace;
            }

        }

        return array;
    }



    private String getGroupStatus(int card_value){

        String group_value = INVALID;

        if(card_value == IMPURE_SEQUENCE_VALUE)
            group_value = IMPURE_SEQUENCE;
        else
        if(card_value == PURE_SEQUENCE_VALUE)
            group_value = PURE_SEQUENCE;
        else if(card_value == SET_VALUE)
            group_value = SET;
        else
            group_value = INVALID;

        return group_value;
    }

    private int getGroupPoints(final ArrayList<CardModel> arrayList){
        int $sum = 0;
        String $joker_num = "" + joker_card.substring(2);
        for (int i = 0; i < arrayList.size() ; i++) {

            String current_card = arrayList.get(i).Image;

            if(current_card.equalsIgnoreCase(DUMMY_CARD))
                continue;

            if(current_card.equalsIgnoreCase(MAIN_JOKER_CARD) || current_card.equalsIgnoreCase(MAIN_JOKER_CARD_2))
                current_card = joker_card;


            String $card2_color = getColorCode(current_card);
            String $card2_num = getCardNumber(current_card);


            if($card2_num.equalsIgnoreCase($joker_num))
            {
                $card2_color = JOKER_CARD;
                $card2_num = "0";
            }


            $card2_num = ConvertSpecialtoNumber($card2_num);


            int card2 = Integer.parseInt($card2_num);

            if(card2 > 10)
                card2 = 10;

            $sum = $sum+card2;

        }

        return $sum;
    }

    private ArrayList<CardModel> setValueList(final ArrayList<CardModel> arrayList,int $rule,String group_points,String group_value){

        for (int i = 0; i < arrayList.size() ; i++) {

            CardModel model = arrayList.get(i);

            model.group_value_params = group_params;
            model.group_value_response = ""+$rule;

            model.group_value = ""+group_value;
            model.group_points = ""+group_points;
            model.value_grp = $rule;

        }

        return  arrayList;
    }

    private void InvalidGroup(ArrayList<CardModel> arrayList){
        for (int i = 0; i < arrayList.size() ; i++) {

            CardModel model = arrayList.get(i);

            int card_value = 0;

            model.group_value = INVALID;

            model.value_grp = card_value;
            model.group_value_params = group_params;
            model.group_value_response = ""+card_value;

        }

    }

    private String convertCardIntoNumber(String current_card){


        String $joker_num = "" + joker_card.substring(2);

        if(current_card.equalsIgnoreCase(MAIN_JOKER_CARD) || current_card.equalsIgnoreCase(MAIN_JOKER_CARD_2))
            current_card = joker_card;


        if(current_card.equalsIgnoreCase(DUMMY_CARD))
            return ""+0;

        String $card_num = getCardNumber(current_card);


        if($card_num.equalsIgnoreCase($joker_num))
        {
            $card_num = "0";
        }


        $card_num = ConvertSpecialtoNumber($card_num);



        return $card_num;
    }


    private String getColorCode(String card_name){
        return card_name.substring(0, 2);
    }

    private String getCardNumber(String card_name){
        return card_name.substring(2);
    }

    private String ConvertSpecialtoNumber(String $card1_num_set){

        $card1_num_set = $card1_num_set.equalsIgnoreCase("J") ? "11" : $card1_num_set;
        $card1_num_set = $card1_num_set.equalsIgnoreCase("Q") ? "12" : $card1_num_set;
        $card1_num_set = $card1_num_set.equalsIgnoreCase("K") ? "13" : $card1_num_set;
        $card1_num_set = $card1_num_set.equalsIgnoreCase("A") ? "14" : $card1_num_set;


        return $card1_num_set;
    }

    CardModel card1;
    CardModel card2;
    CardModel card3;
    CardModel card4;
    CardModel card5;
    CardModel card6;
    CardModel card7;
    CardModel card8;
    CardModel card9;
    CardModel card10;

    private boolean isColorMatch() {

        String emptyCards = "null";
        String CardColorArray[] = {card1.getCardColor(),card2.getCardColor()
                ,card3.getCardColor(),card4 != null ? card4.getCardColor() : emptyCards,card5 != null ? card5.getCardColor() : emptyCards
                ,card6 != null ? card6.getCardColor() : emptyCards,card7 != null ? card7.getCardColor() : emptyCards,card8 != null ? card8.getCardColor() : emptyCards
                ,card9 != null ? card9.getCardColor() : emptyCards,card10 != null ? card10.getCardColor() : emptyCards};

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

    private boolean straightCard(){
        boolean isStraight = false;
        Integer emptyCards = -1;
        List<Integer> cardNumberList = new ArrayList<>();
        Integer cardNumberArray[] = {card1.getCardNumber(),card2.getCardNumber()
                ,card3.getCardNumber(),card4 != null ? card4.getCardNumber() : emptyCards,card5 != null ? card5.getCardNumber() : emptyCards
                ,card6 != null ? card6.getCardNumber() : emptyCards,card7 != null ? card7.getCardNumber() : emptyCards,card8 != null ? card8.getCardNumber() : emptyCards
                ,card9 != null ? card9.getCardNumber() : emptyCards,card10 != null ? card10.getCardNumber() : emptyCards};

        for (Integer cardnumber: cardNumberArray) {
            if(cardnumber != -1)
                cardNumberList.add(cardnumber);
        }

        Collections.reverse(cardNumberList);

        if(checkSequence(cardNumberList,0))
        {
            isStraight = true;
        }

        return isStraight;
    }

    private boolean checkSequence(List<Integer> $arr,int $joker_count){
        boolean $sequence = true;
        int $total_card = $arr.size();

        for (int j = 0; j < $arr.size() - 1; j++) {

            int $val = $arr.get(j);

            if($val == -1)
                break;

            if($val!=0 && $total_card>(j+1))
            {
                int card_gap = $arr.get(j + 1) - ($val+1);
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
