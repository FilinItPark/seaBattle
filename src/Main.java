import java.util.Random;
import java.util.Scanner;

/**
 * @author 1ommy
 * @version 27.08.2023
 */
public class Main {

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

        //region инициализация переменных
        Random random = new Random();

        Cell[][] computerField, userField;
        Player activePlayer, winner = Player.INITIAL;

        int fieldSize = 0;
        int countUserShips, countCompShips;

        Scanner scanner = new Scanner(System.in);

        boolean isPlay = true;

        boolean isInputCorrect;
        //endregion

        //region ввод размер поля,заполнение пустой клеткой
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

        userField = new Cell[fieldSize][fieldSize];
        computerField = new Cell[fieldSize][fieldSize];

        countUserShips = countCompShips = 10;

        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                userField[i][j] = Cell.EMPTY;
                computerField[i][j] = Cell.EMPTY;
            }
        }
        //endregion

        //region расстановка кораблей на поле

        for (int k = 0; k < countUserShips; k++) {
            int iShip, jShip;

            do {
                iShip = random.nextInt(fieldSize);
                jShip = random.nextInt(fieldSize);
            } while (userField[iShip][jShip] != Cell.EMPTY);

            userField[iShip][jShip] = Cell.ALIVE_SHIP;
        }

        for (int k = 0; k < countCompShips; k++) {
            int iShip, jShip;

            do {
                iShip = random.nextInt(fieldSize);
                jShip = random.nextInt(fieldSize);
            } while (computerField[iShip][jShip] != Cell.EMPTY);

            computerField[iShip][jShip] = Cell.ALIVE_SHIP;
        }

        //endregion

        if (random.nextInt(1000) > 500) {
            activePlayer = Player.USER;
        } else {
            activePlayer = Player.COMPUTER;
        }

        //region стреляем
        while (isPlay) {
            for (int i = 0; i < 100; i++) {
                System.out.println();
            }

            //поле компьютера
            for (int i = 0; i < fieldSize; i++) {
                for (int j = 0; j < fieldSize; j++) {
                    if (computerField[i][j] == Cell.ALIVE_SHIP) {
                        System.out.print(Cell.ALIVE_SHIP.getValue());
                    } else {
                        System.out.print(Cell.EMPTY.getValue());
                    }
                }
                System.out.println();
            }
            System.out.println();

            //поле игрока
            for (int i = 0; i < fieldSize; i++) {
                for (int j = 0; j < fieldSize; j++) {
                    if (userField[i][j] == Cell.ALIVE_SHIP) {
                        System.out.print(Cell.ALIVE_SHIP.getValue());
                    } else {
                        System.out.print(Cell.EMPTY.getValue());
                    }
                }
                System.out.println();

            }

            System.out.println(activePlayer);
            if (activePlayer == Player.COMPUTER) {
                System.out.println("Компьютер стреляет");

                int iShoot = random.nextInt(fieldSize);
                int jShoot = random.nextInt(fieldSize);

                if (userField[iShoot][jShoot] != Cell.ALIVE_SHIP) {
                    userField[iShoot][jShoot] = Cell.MISS_SHOOT;
                    activePlayer = Player.USER;
                } else if (userField[iShoot][jShoot] == Cell.ALIVE_SHIP) {
                    userField[iShoot][jShoot] = Cell.DEAD_SHIP;
                    countUserShips--;
                    System.out.println("Человек потерял корабль. Осталось живых: " + countUserShips);
                }
            } else if (activePlayer == Player.USER) {
                System.out.println("Игрок стреляет");

                int iShoot = random.nextInt(fieldSize);
                int jShoot = random.nextInt(fieldSize);

                if (computerField[iShoot][jShoot] != Cell.ALIVE_SHIP) {
                    computerField[iShoot][jShoot] = Cell.MISS_SHOOT;
                    activePlayer = Player.COMPUTER;
                } else if (computerField[iShoot][jShoot] == Cell.ALIVE_SHIP) {
                    computerField[iShoot][jShoot] = Cell.DEAD_SHIP;
                    countCompShips--;
                    System.out.println("Компьютер потерял корабль. Осталось живых: " + countCompShips);

                }
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