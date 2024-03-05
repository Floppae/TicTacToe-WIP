

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    static char emptySpaceSymbol = ' ';
    static char playerOneSymbol = 'X';
    static char playerTwoSymbol = 'O';
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean done=false;
        while (!done) {
            System.out.println("Welcome to TicTacToe!");
            System.out.println("Please select an option from the list below:");
            System.out.println("1: Single Player");
            System.out.println("2: Two Player");
            System.out.println("D: Display Last Match");
            System.out.println("Q: Quit");
            String option=in.nextLine().toUpperCase();
            switch(option){
                case "1":
                    System.out.println("Enter player 1's name:");
                    String[] playerNames={in.next(),"computer"};
                    runOnePlayerGame(playerNames);
                    break;
                case "2":
                    System.out.println("Enter the player names:");
                    String[] twoPlayerNames={in.next(),in.next()};
                    runTwoPlayerGame(twoPlayerNames);
                    break;
                case "D":
                    System.out.println("Game History:");
                    String[] historyPlayerNames={in.next(),in.next()};
                    ArrayList<char[][]> gameHistory = new ArrayList<>();
                    runGameHistory(historyPlayerNames,gameHistory);
                    break;
                case "Q":
                    System.out.println("Thank you for playing!");
                    done=true;
            }
        }
    }
    private static String displayGameFromState(char[][] state) {
        StringBuilder current=new StringBuilder();
        for(int i=0;i<3;i++) {
            for (int j = 0; j < 3; j++) {
                current.append(state[i][j]).append(emptySpaceSymbol);
            }
            current.append("\n");
        }
        return current.toString();
    }
    private static char[][] getInitialGameState() {
        return new char[][]{{emptySpaceSymbol, emptySpaceSymbol, emptySpaceSymbol},
                {emptySpaceSymbol, emptySpaceSymbol, emptySpaceSymbol},
                {emptySpaceSymbol, emptySpaceSymbol, emptySpaceSymbol}};
    }
    private static ArrayList<char[][]> runTwoPlayerGame(String[] playerNames) {
        char[][] currentState = getInitialGameState();
        Random rng = new Random();
        int playersTurn = rng.nextInt(2);
        ArrayList<char[][]> gameHistory = new ArrayList<>();

        System.out.println("Two Player mode selected");
        System.out.println("Flipping a coin to see who goes first!");
        System.out.println(playerNames[playersTurn] + " will go first. They will use:" + playerOneSymbol);

        while (true) {
            currentState = runPlayerMove(playerNames[0], playerOneSymbol, currentState);
            gameHistory.add(currentState);
            System.out.println(displayGameFromState(currentState));
            if (checkDraw(currentState) || checkWin(currentState)) {
                break;
            }

            currentState = runPlayerMove(playerNames[1], playerTwoSymbol, currentState);
            gameHistory.add(currentState);
            System.out.println(displayGameFromState(currentState));
            if (checkDraw(currentState) || checkWin(currentState)) {
                break;
            }
        }

        return gameHistory;
    }
    private static ArrayList<char[][]> runOnePlayerGame(String[] playerNames) {
        char[][] currentState = getInitialGameState();
        Random rng = new Random();
        int playersTurn = rng.nextInt(2);
        ArrayList<char[][]> gameHistory = new ArrayList<>();

        System.out.println("One Player mode selected");
        System.out.println("Flipping a coin to see who goes first!");
        System.out.println(playerNames[playersTurn] + " will go first. They will use: " + playerOneSymbol);

        while (!checkDraw(currentState) && !checkWin(currentState)) {
            char[][] nextBoard;

            if (playersTurn==0) {
                nextBoard=runPlayerMove(playerNames[0],playerOneSymbol,currentState);
                System.out.println(displayGameFromState(nextBoard));
            }
            else {
                nextBoard = getCPUMove(currentState);
                System.out.println("Computer's turn:");
                System.out.println(displayGameFromState(nextBoard));
            }
            gameHistory.add(nextBoard);
            currentState=nextBoard;
            playersTurn=1-playersTurn;
        }
        return gameHistory;
    }
    private static char[][] runPlayerMove(String playerName, char playerSymbol, char[][] currentState) {
        boolean finished=false;
        Scanner in = new Scanner(System.in);
        while(!finished){
            System.out.println(playerName+", please enter your move in row, column form");
            int r=in.nextInt();
            int c=in.nextInt();
            if (r<0 || r>2 || c<0 || c>2) {
                System.out.println("INVALID MOVE. Please enter numbers between 0 and 2");
                continue;
            }
            else if(currentState[r][c]!=' '){
                System.out.println("INVALID MOVE. Spot was already chosen");
                continue;
            }
            currentState[r][c]=playerSymbol;
            finished=true;
        }
        return currentState;
    }
    private static int[] getInBoundsPlayerMove(String playerName) {
        int[] move = new int[2];
        Scanner in = new Scanner(System.in);

        do {
            System.out.println(playerName + " please enter your move in row, column form");
            int r=in.nextInt();
            int c=in.nextInt();
            if (r>=0 && r<=2 && c>=0 && c<=2) {
                move[0]=r;
                move[1]=c;
                break;
            }
            else {
                System.out.println("INVALID MOVE. Please enter numbers between 0 and 2");
            }
        } while (true);
        return move;
    }
    private static boolean checkValidMove(int[] move, char[][] state) {
        return state[move[0]][move[1]] == emptySpaceSymbol;
    }
    private static char[][] makeMove(int[] move, char symbol, char[][] currentState) {
        char[][] board = new char[currentState.length][];

        for (int i=0;i<currentState.length;i++) {
            board[i] = Arrays.copyOf(currentState[i],currentState[i].length);
        }
        board[move[0]][move[1]]=symbol;
        return board;
    }
    private static boolean checkWin(char[][] state) {
        for (int i=0;i<state.length;i++) {
            if (state[i][0]!=' ' && state[i][0]==state[i][1] && state[i][1]==state[i][2]) {
                System.out.println("winner!");
                return true;
            }
        }
        for (int j=0;j<state[0].length;j++) {
            if (state[0][j]!=' ' && state[0][j]==state[1][j] && state[1][j]==state[2][j]) {
                System.out.println("winner!");
                return true;
            }
        }
        if (state[0][0]!=' ' && state[0][0]==state[1][1] && state[1][1]==state[2][2]) {
            System.out.println("winner!");
            return true;
        }
        if (state[0][2]!=' ' && state[0][2]==state[1][1] && state[1][1]==state[2][0]) {
            System.out.println("winner!");
            return true;
        }

        return false;
    }

    private static boolean checkDraw(char[][] state) {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                if (state[i][j] == ' ') {
                    return false;
                }
            }
        }
        System.out.println("draw");
        return true;
    }

    private static char[][] getCPUMove(char[][] gameState) {
        ArrayList<int[]> validSpot = getValidMoves(gameState);

        for (int[] move:validSpot) {
            char[][] newBoard = makeMove(move, playerTwoSymbol, gameState);
            if (checkWin(newBoard)) {
                return newBoard;
            }
        }
        for (int[] move:validSpot) {
            char[][] newBoard = makeMove(move, playerTwoSymbol, gameState);
            if (checkWin(newBoard)) {
                return newBoard;
            }
        }
        if (gameState[1][1] == ' ') {
            return makeMove(new int[]{1, 1}, playerTwoSymbol, gameState);
        }
        for (int[] move:validSpot) {
            if ((move[0]==0 || move[0]==2) && (move[1]==0 || move[1]==2)) {
                return makeMove(move, playerTwoSymbol, gameState);
            }
        }
        return makeMove(validSpot.get(0), playerTwoSymbol, gameState);
    }
    private static ArrayList<int[]> getValidMoves(char[][] gameState) {
        ArrayList<int[]> validSpots = new ArrayList<>();
        int rows = gameState.length;
        int columns = gameState[0].length;
        for (int r=0;r<rows;r++) {
            for (int c=0;c<columns;c++) {
                if (gameState[r][c]==emptySpaceSymbol) {
                    validSpots.add(new int[]{r, c});
                }
            }
        }
        return validSpots;
    }
    private static void runGameHistory(String[] playerNames, ArrayList<char[][]> gameHistory) {
        //I have tried to complete this section, but too many errors keep occurring.
        //I cannot do this question and will get help from my friends to understand it at a later point in time.
    }
}