package com.metagards.metagames.Details;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.BaseActivity;
import com.metagards.metagames.Details.Adapter.GameDetailsAdapter;
import com.metagards.metagames.Details.Menu.DialogGameHistory;
import com.metagards.metagames.Details.Menu.DialogRedeemHistory;
import com.metagards.metagames.Details.Menu.DialogReferralLevel;
import com.metagards.metagames.Details.Menu.DialogReferralUser;
import com.metagards.metagames.Details.Menu.DialogReferralUserPurch;
import com.metagards.metagames.Details.Menu.DialogTransactionHistory;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;

import java.util.ArrayList;
import java.util.List;

public class GameDetails_A extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        initGameDetailsList();

    }

    private void initGameDetailsList() {

        RecyclerView recyclerView = findViewById(R.id.recDetailsList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        GameDetailsAdapter detailsAdapter = new GameDetailsAdapter(this);

        detailsAdapter.onItemSelectListener(new OnItemClickListener() {
            @Override
            public void Response(View v, int position, Object object) {
                DialogGameHistory dialogGameHistory = new DialogGameHistory(GameDetails_A.this);
                DialogRedeemHistory dialogRedeemHistory = new DialogRedeemHistory(GameDetails_A.this);
                DialogTransactionHistory dialogTransactionHistory = new DialogTransactionHistory(GameDetails_A.this);
                DialogReferralUser dialogReferralUser =  new DialogReferralUser(GameDetails_A.this);
                DialogReferralUserPurch dialogReferralUserPurch =  new DialogReferralUserPurch(GameDetails_A.this);
                DialogReferralLevel dialogReferralLevel =  new DialogReferralLevel(GameDetails_A.this);

                if(position == 0)
                {
                    dialogGameHistory.show();
                }
                else if(position == 1)
                {
                    dialogRedeemHistory.show();
                }
                else if(position == 2)
                {
                    dialogTransactionHistory.show();
                }
                else if(position == 3)
                {
                    dialogReferralUser.show();
                }
                else if(position == 4)
                {
                    dialogReferralUserPurch.show();
                }
                else if(position == 5)
                {
                    dialogReferralLevel.show();
                }
            }
        });

        List<GameDetailsModel> gameDetailsModels = new ArrayList<>();

        gameDetailsModels.add(new GameDetailsModel("1","Games History",R.drawable.ic_game_console));
        gameDetailsModels.add(new GameDetailsModel("2","Redeems",R.drawable.ic_game_redeem));
        gameDetailsModels.add(new GameDetailsModel("3","Transactions",R.drawable.ic_game_transaction));
        gameDetailsModels.add(new GameDetailsModel("4","Referral",R.drawable.ic_game_transaction));
        gameDetailsModels.add(new GameDetailsModel("5","Referral Purchase",R.drawable.ic_game_transaction));
        gameDetailsModels.add(new GameDetailsModel("6","Level Wise Refer",R.drawable.ic_game_transaction));

        detailsAdapter.setArrayList(gameDetailsModels);
        recyclerView.setAdapter(detailsAdapter);

    }

    public void onBack(View view) {
        onBackPressed();
    }

}