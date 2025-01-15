package com.first_player_games.Api;

public class EndPoints {

    public static String SERVER_PATH = "ws://games.androappstech.in:1234";

    private static final String API = "api/";

    public static final String email_login = API+ "User/email_login";
    public static final String user_profile = API+ "User/profile";


    public static final String get_table_master = API+ "Ludo/get_table_master";
    public static final String get_table = API+ "Ludo/get_table";
    public static final String join_table = API+ "Ludo/join_table";
    public static final String start_game = API+ "Ludo/start_game";
    public static final String make_winner = API+ "Ludo/make_winner";
    public static final String leave_table = API+ "Ludo/leave_table";
    public static final String gameStatus = API+ "Ludo/status";
    public static final String rolldice = API+ "/rolldice";


    public static final String token = "c7d3965d49d4a59b0da80e90646aee77548458b3377ba3c0fb43d5ff91d54ea28833080e3de6ebd4fde36e2fb7175cddaf5d8d018ac1467c3d15db21c11b6909";
}
