
package com.example;

import java.util.Random;
import java.util.Scanner;

public final class App {
    private static final int BOARD_SIZE = 9;
    private static final int BOARD_WIDTH = 3;
    private static final int FIRST_CELL_NUMBER = 1;
    private static final int INVALID_MOVE = -1;

    private static final char PLAYER_MARK = 'X';
    private static final char COMPUTER_MARK = 'O';

    private static final String BOARD_BORDER = "+-----+-----+-----+";
    private static final String INVALID_INPUT_MESSAGE =
            "Invalid input. Enter a number from 1 to 9.";

    private static final int[][] WINNING_LINES = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    private App() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            playGame(scanner, new Random());
        }
    }

    private static void playGame(Scanner scanner, Random random) {
        char[] board = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};

        int lastPlayerMove = INVALID_MOVE;
        int lastComputerMove = INVALID_MOVE;

        System.out.println("=== TIC-TAC-TOE ===");
        System.out.println("You: X | Computer: O");
        System.out.println("[X] and [O] highlight each side's latest move.");

        printBoard(board, lastPlayerMove, lastComputerMove);

        while (true) {
            int playerMove = readPlayerMove(scanner, board);

            if (playerMove == INVALID_MOVE) {
                System.out.println("Input ended. Game stopped.");
                return;
            }

            board[playerMove] = PLAYER_MARK;
            lastPlayerMove = playerMove;

            System.out.println(
                    "\nYour move: X in cell " + (playerMove + FIRST_CELL_NUMBER)
            );

            printBoard(board, lastPlayerMove, lastComputerMove);

            if (hasWinner(board, PLAYER_MARK)) {
                printResult("You won the game!");
                return;
            }

            if (isBoardFull(board)) {
                printResult("It's a draw!");
                return;
            }

            int computerMove = makeComputerMove(board, random);
            lastComputerMove = computerMove;

            System.out.println(
                    "Computer move: O in cell "
                            + (computerMove + FIRST_CELL_NUMBER)
            );

            printBoard(board, lastPlayerMove, lastComputerMove);

            if (hasWinner(board, COMPUTER_MARK)) {
                printResult("You lost the game!");
                return;
            }

            if (isBoardFull(board)) {
                printResult("It's a draw!");
                return;
            }
        }
    }

    private static int readPlayerMove(Scanner scanner, char[] board) {
        while (true) {
            System.out.print("Choose a cell (1-9): ");

            if (!scanner.hasNextLine()) {
                return INVALID_MOVE;
            }

            String input = scanner.nextLine().trim();
            int position;

            try {
                position = Integer.parseInt(input) - FIRST_CELL_NUMBER;
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT_MESSAGE);
                continue;
            }

            if (position < 0 || position >= board.length) {
                System.out.println(INVALID_INPUT_MESSAGE);
                continue;
            }

            if (!isFree(board[position])) {
                System.out.println(
                        "That cell is already taken. Choose another."
                );
                continue;
            }

            return position;
        }
    }

    private static int makeComputerMove(char[] board, Random random) {
        int[] freePositions = new int[BOARD_SIZE];
        int freeCount = 0;

        for (int i = 0; i < board.length; i++) {
            if (isFree(board[i])) {
                freePositions[freeCount] = i;
                freeCount++;
            }
        }

        int position = freePositions[random.nextInt(freeCount)];

        board[position] = COMPUTER_MARK;

        return position;
    }

    private static boolean isFree(char cell) {
        return cell != PLAYER_MARK && cell != COMPUTER_MARK;
    }

    private static boolean isBoardFull(char[] board) {
        for (char cell : board) {
            if (isFree(cell)) {
                return false;
            }
        }

        return true;
    }

    private static boolean hasWinner(char[] board, char mark) {
        for (int[] line : WINNING_LINES) {
            if (board[line[0]] == mark
                    && board[line[1]] == mark
                    && board[line[2]] == mark) {
                return true;
            }
        }

        return false;
    }

    private static void printBoard(
            char[] board,
            int lastPlayerMove,
            int lastComputerMove
    ) {
        System.out.println();
        System.out.println(BOARD_BORDER);

        for (int row = 0; row < BOARD_WIDTH; row++) {
            StringBuilder rowText = new StringBuilder("|");

            for (int column = 0; column < BOARD_WIDTH; column++) {
                int position = row * BOARD_WIDTH + column;

                rowText.append(
                        formatCell(
                                board,
                                position,
                                lastPlayerMove,
                                lastComputerMove
                        )
                );

                rowText.append('|');
            }

            System.out.println(rowText);
            System.out.println(BOARD_BORDER);
        }

        System.out.println();
    }

    private static String formatCell(
            char[] board,
            int position,
            int lastPlayerMove,
            int lastComputerMove
    ) {
        if (position == lastPlayerMove || position == lastComputerMove) {
            return " [" + board[position] + "] ";
        }

        return "  " + board[position] + "  ";
    }

    private static void printResult(String message) {
        System.out.println(message);
        System.out.println(
                "Created by Shreyas Saha. Thanks for playing!"
        );
    }
}
