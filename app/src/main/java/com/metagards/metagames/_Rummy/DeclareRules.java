package com.metagards.metagames._Rummy;

import java.util.List;

public class DeclareRules {

    List<Integer> groupvalueList;

    int IMPURE_SEQUENCE_VALUE = 4;
    int PURE_SEQUENCE_VALUE = 5;
    int SET_VALUE = 6;

    public DeclareRules(List<Integer> groupvalueList) {
        this.groupvalueList = groupvalueList;
    }

    public boolean isRuleMatch()
    {
        boolean isRulesMatch = false;
        int isPureOrImpureSeq = 0;
        for (Integer value: groupvalueList) {

            if((value == IMPURE_SEQUENCE_VALUE || value == PURE_SEQUENCE_VALUE) && isPureOrImpureSeq == 0)
            {
                isPureOrImpureSeq = value;
            }
            else
            if(isPureOrImpureSeq == PURE_SEQUENCE_VALUE)
            {

                if(value == PURE_SEQUENCE_VALUE || value == IMPURE_SEQUENCE_VALUE)
                {
                    isRulesMatch = true;
                }
            }
            else if(isPureOrImpureSeq == IMPURE_SEQUENCE_VALUE)
            {

                if(value == PURE_SEQUENCE_VALUE)
                {
                    isRulesMatch = true;
                }

            }

        }

        return isRulesMatch;
    }

}
