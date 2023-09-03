import java.util.Random;
import java.util.Scanner;

/**
 * @author 1ommy
 * @version 27.08.2023
 */
public class Main {
    static Random random = new Random();
    static int fieldSize = 0;
    static int countUserShips = 0;
    static int countCompShips = 0;
    static Player activePlayer;


    public enum Cell {
        ALIVE_SHIP('Ж'),
        DEAD_SHIP('Х'),
        MISS_SHOOT('0'),
        EMPTY('.');

        char value;

        Cell(char value) {
            this.value = value;
        }

        public char getValue() {
            return value;
        }
    }

    public enum Player {
        COMPUTER("Компьютер"),
        USER("Человек"),
        INITIAL("Пока неизвестно");

        String value;

        Player(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    static void cleanConsole() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
    static void initEmptyField(Cell[][] userField, Cell[][] computerField) {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                userField[i][j] = Cell.EMPTY;
                computerField[i][j] = Cell.EMPTY;
            }
        }
    }

    static void printField(Cell[][] field) {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (field[i][j] == Cell.ALIVE_SHIP) {
                    System.out.print(Cell.ALIVE_SHIP.getValue());
                } else {
                    System.out.print(Cell.EMPTY.getValue());
                }
            }
            System.out.println();
        }
    }


    static int getFieldSize() {
        Scanner scanner = new Scanner(System.in);

        boolean isInputCorrect;
        System.out.println("Введите размер поля");
        do {
            isInputCorrect = true;

            try {
                fieldSize = scanner.nextInt();

                if (fieldSize < 10) {
                    throw new Exception();
                }
            } catch (Exception exception) {
                isInputCorrect = false;
                System.out.println("Ошибка.Минимальный размер поля 10 на 10");
            }
        } while (isInputCorrect == false);
        return fieldSize;
    }

    static void setUpBattleShipsOnTheMap(int countShips, Cell[][] field) {
        for (int k = 0; k < countShips; k++) {
            int iShip, jShip;

            do {
                iShip = random.nextInt(fieldSize);
                jShip = random.nextInt(fieldSize);
            } while (field[iShip][jShip] != Cell.EMPTY);

            field[iShip][jShip] = Cell.ALIVE_SHIP;
        }
    }

    static void shoot(Cell[][] field, Player newPlayer) {
        int iShoot = random.nextInt(fieldSize);
        int jShoot = random.nextInt(fieldSize);

        if (field[iShoot][jShoot] != Cell.ALIVE_SHIP) {
            field[iShoot][jShoot] = Cell.MISS_SHOOT;
            activePlayer = newPlayer;
        } else if (field[iShoot][jShoot] == Cell.ALIVE_SHIP) {
            field[iShoot][jShoot] = Cell.DEAD_SHIP;
            if (newPlayer == Player.USER)
                countUserShips--;
            else if (newPlayer == Player.COMPUTER)
                countCompShips--;
//            System.out.println(newPlayer.value + " потерял корабль. Осталось живых: " + countUserShips);
        }
    }

    public static void main(String[] args) {
        /*
        1) инициализация переменных (рандом,игрок,размер поля, сколько кораблей у компа и у игрока,кто победил по итогу)
        2) вводим размеры поля
        3) рандомно расставляем корабли
        4) кто стреляет ? (игрок/комп)
        5) выстрел
        6) проверка попадания - если попали,то тот же игрок ходит еще раз,пока не промахнется
        7) проверка выиграша - если никто не выиграл,стреляем заново ( возвращаемся на шаг 5)
        8) вывод победителя, ура

        вначале расставим игрока (вначале 4х, потом 3, потом ...)
        затем расставим компьюетера

        1) определяем рандомом i,j. есть ли место сверху/снизу, чтобы разместить по вертикали. проверяем по горизонтали.
         если места нет на одном из направлений,ставим на другом.
         если есть на обоих,определяем рандомом куда ставить.

        1 4хпалубный корабль
        2 3х палубных
        3 2х палубных
        4 однопалубных
         */

        Cell[][] computerField, userField;
        Player winner = Player.INITIAL;

        boolean isPlay = true;

        fieldSize = getFieldSize();

        userField = new Cell[fieldSize][fieldSize];
        computerField = new Cell[fieldSize][fieldSize];

        countUserShips = countCompShips = 10;

        initEmptyField(userField, computerField);

        setUpBattleShipsOnTheMap(countUserShips, userField);
        setUpBattleShipsOnTheMap(countCompShips, computerField);

        if (random.nextInt(1000) > 500) {
            activePlayer = Player.USER;
        } else {
            activePlayer = Player.COMPUTER;
        }


        while (isPlay) {
            cleanConsole();

            printField(computerField);
            System.out.println();
            printField(userField);

            System.out.println(activePlayer);
            if (activePlayer == Player.COMPUTER) {
                shoot(userField, Player.USER);
            } else if (activePlayer == Player.USER) {
                shoot(computerField, Player.COMPUTER);
            }

            if (countUserShips == 0) {
                winner = Player.COMPUTER;
                isPlay = false;
            } else if (countCompShips == 0) {
                winner = Player.USER;
                isPlay = false;
            }

        }

        System.out.println(String.format("Победил %s", winner.getValue()));
    }


}