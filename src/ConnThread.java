//LULSEGED ADMASU 2022

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ConnThread extends Thread{
    private DataInputStream instream;
    private  DataOutputStream  outstream;
    private PrintWriter out;      // the printwriter will allow us to use print( ) and println( )  methods
    private BufferedReader in;  // the BufferedReader will allow us to use readLine( ) method
    private Random rand;
    private static char[][] board;
    private static int row,col;
    private Socket s;

    ConnThread(Socket s) {
        this.s = s;
        rand = new Random();
        try {
            instream = new  DataInputStream(s.getInputStream());
            outstream = new DataOutputStream(s.getOutputStream());
            out = new PrintWriter(outstream, true);
            in = new BufferedReader(new InputStreamReader(instream));
        }catch (IOException e) {
            System.err.println("ConnThread: " + e);
        }

        board = new char[4][4];
        for (int x = 0; x <= 3; x++) {
            for (int y = 0; y <= 3; y++) {
                board[x][y] = ' ';
            }
        }
        row = -1;
        col = -1;
    }

    public Socket getS() {
        return s;
    }

    public void run () {
        int counter = 0;
        String response = "";
        boolean gameOver = false;
        boolean turn = true;

        if(rand.nextInt(2) + 1 == 1) {
            turn = false;
            out.println("CLIENT");
        }
        else
            turn = true;

        do {
                if (!turn) {
                    try {
                        response = in.readLine();
                        System.out.println("\n*****CLIENT's-MOVE*****");
                    }
                    catch (IOException e ) {
                        System.err.println("Some sort of read error on socket in server thread " + e);
                        System.exit(-1);
                    }
                    String [] data = response.split( "\\s+" );
                    row = Integer.parseInt(data[1]);
                    col = Integer.parseInt(data[2]);
                    board[row][col] = 'O';
                    printboard();
                    counter++;
                    if (checkwin() || counter == 16) {
                        gameOver = true;
                        if (checkwin())
                            out.println("MOVE -1 -1 WIN");
                        else
                            out.println("MOVE -1 -1 TIE");
                    }
                }
                else  {   // this is the computer/server’s move code
                    System.out.println("\n*****SERVER's-MOVE*****");
                    makemove();
                    counter++;
                    board[row][col] = 'X';
                    printboard();
                    if (checkwin()  || counter == 16) {
                        gameOver = true;
                        if (checkwin())
                            out.println("MOVE " + row + " " + col + " LOSS");
                        else
                            out.println("MOVE " + row + " " + col + " TIE");
                    }
                    else {
                        out.println("MOVE " + row + " " + col);
                    }
                }
        turn = !turn;
        } while (!gameOver);
    }


    void makemove() {
        int x = 4;
        row = rand.nextInt(x);
        col = rand.nextInt(x);
        while(board[row][col] != ' ') {
            row = rand.nextInt(x);
            col = rand.nextInt(x);
        }

    }
    boolean checkwin() {
        for (int x = 0; x <= 3; x++)   
             {
                 // check for a row-win
                 if (board[x][0] == board[x][1] && board[x][1] == board[x][2] &&
                         board[x][2] == board[x][3]  && board[x][0] != ' ')
                     return true;
                 // check for a col-win
                 else if (board[0][x] == board[1][x] && board[1][x] == board[2][x] &&
                         board[2][x] == board[3][x]  && board[0][x] != ' ')
                     return true;
             }

        // check for a diagonal-win
        if (board[0][0] == board[1][1] && board[2][2] == board[3][3] && board[0][0] == board[3][3] && board[0][0] != ' ' ||
                board[3][0] == board[2][1] && board[1][2] == board[0][3] && board[3][0] == board[0][3] && board[3][0] != ' ')
                return true;
        return false;
    }

    public static void printboard() {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                if(i != 3) {
                    if(board[j][i] == ' ')
                        System.out.print(board[j][i] + "\t|");
                    else {
                        if (i == 0)
                            System.out.print("  " + board[j][i] + "\t|");
                        else
                            System.out.print(" " + board[j][i] + " |");
                    }
                }
                else
                    System.out.print(" " + board[j][i]);
            }
            if(j < 3) System.out.print("\n ---------------\n");
        }
        System.out.print("\n\n");
    }
}



