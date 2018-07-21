package com.webfluxwebsocket.game.web.rest;

import com.webfluxwebsocket.game.domain.Player;
import com.webfluxwebsocket.game.domain.enumeration.PlayerState;
import com.webfluxwebsocket.game.service.GameEngineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.DecimalFormat;
import java.util.List;

public class WebFluxWebSocketPlayerStateIntegrationTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(WebFluxWebSocketPlayerStateIntegrationTest.class);

    @Autowired
    private GameEngineService gameEngineService;

    private final static int NUMBER_FROM = 0;                                        //todo
    private final static int   NUMBER_TO = 10;                                       //todo: ( если количество разных ставок игроков будет равнятся этому - тогда гарантированно получим 1-выиграш )
    private final static long TIME_ROUND = 20000l;                                   //todo
    private final static int      ROUNDS = 2;                                        //todo
    private final static double  WIN_MIN = 100 * 1 / (((double)NUMBER_TO * 2) - 1);  //todo: минимальный выиграш для максимум игроков
    private final static double  WIN_MAX = 100 * 2 / ((double)NUMBER_TO + 1);        //todo: максимальный выиграш для минимум гроков

    private int monitorMinThrowOfNumber = 999;
    private int monitorMaxThrowOfNumber = -999;
    private int      monitorAllRequests = 0;
    private int monitorAcceptedRequests = 0;
    private int monitorCanceledRequests = 0;
    private int       monitorAllPlayers = -1;
    private int   monitorPlayingPlayers = -1;
    private int       monitorWinPlayers = -1;
    private int      monitorLeftPlayers = -1;

    private int     players;
    private int    requests;
    private double acceptedRequests;  //todo: процент принятых запросов зависит от количества всех игроков и количества всех запросов
    private double canceledRequests;  //todo: процент отклоненных запросов - остаток от принятых запросов

    /**
     * Тест генератора случайных чисел
     * *******************************
     * Числа должны генерится случайнам образом в диапазоне от 0 до 9
     */
    @Test
    public void testGeneratedNumber() throws InterruptedException {
        for (int i=0; i<1000; i++) {
            int throwOfNumber = gameEngineService.getThrowOfNumber(NUMBER_FROM, NUMBER_TO);
            monitorMinThrowOfNumber = throwOfNumber<monitorMinThrowOfNumber ? throwOfNumber : monitorMinThrowOfNumber;
            monitorMaxThrowOfNumber = monitorMaxThrowOfNumber<throwOfNumber ? throwOfNumber : monitorMaxThrowOfNumber;
            Thread.sleep(1l);
        }

        GameDataEmulator gameDataEmulator = new GameDataEmulator();

        gameDataEmulator.printGeneratedNumberReport();

        Assertions.assertTrue(gameDataEmulator.isThrowOfNumber, "Generate Random Number");
    }

    /**
     * Тест #1
     * *****************************************************
     * В течении времени для 1-раунда создаются 15-игроков.
     * В течении времени для 1-раунда выполняется всего 20-запросов на 15-игроков, но система игры за 1-раунд обработает 1-запрос для каждого игрока (только 15 с 20).
     * Всего выполняется 2-раунда игры.
     * В результате каждого раунда можно получить только 1-выиграш, с таким расчетом что все игроки делают разные ставки (в зависимости от времени старта с 2-раундов можно получить 1~2 выиграша).
     */
    @Test
    public void quietPlayerEmulation() throws InterruptedException {
        players = 15; //todo
        requests = 20; //todo
        long requestFrequency = 100l; //todo
        acceptedRequests = percentRequests(requests, players);
        canceledRequests = 100 - acceptedRequests;

        new Thread(new GamePlayEmulation()).start();

        for (int round=0; round<ROUNDS; round++) {
            new Thread(new GameEngineEmulator(requestFrequency)).start();
            Thread.sleep(TIME_ROUND);
        }

        GameDataEmulator gameDataEmulator = new GameDataEmulator();

        gameDataEmulator.printPlayerReport();

        Assertions.assertTrue(gameDataEmulator.isAcceptedRequests, "Accepted players bets in the round");
        Assertions.assertTrue(gameDataEmulator.isCanceledRequests, "Canceled players bets in the round");
        Assertions.assertTrue(gameDataEmulator.isPlayingPlayers, "Connected players in the round");
        Assertions.assertTrue(gameDataEmulator.isWinPlayers, "Winners in the round");
    }

    /**
     * Тест #2
     * *****************************************************
     * В течении времени для 1-раунда создаются 150-игроков.
     * В течении времени для 1-раунда выполняется всего 200-запросов на 150-игроков, но система игры за 1-раунд обработает 1-запрос для каждого игрока (только 150 с 200).
     * Всего выполняется 2-раунда игры.
     * В результате каждого раунда можно получить только 1-выиграш, с таким расчетом что все игроки делают разные ставки (в зависимости от времени старта с 2-раундов можно получить 1~2 выиграша).
     */
    @Test
    public void stressPlayerEmulation() throws InterruptedException {
        players = 150; //todo
        requests = 200; //todo
        long requestFrequency = 10l; //todo
        acceptedRequests = percentRequests(requests, players);
        canceledRequests = 100 - acceptedRequests;

        new Thread(new GamePlayEmulation()).start();

        for (int round=0; round<ROUNDS; round++) {
            new Thread(new GameEngineEmulator(requestFrequency)).start();
            Thread.sleep(TIME_ROUND);
        }

        GameDataEmulator gameDataEmulator = new GameDataEmulator();

        gameDataEmulator.printPlayerReport();

        Assertions.assertTrue(gameDataEmulator.isAcceptedRequests, "Accepted players bets in the round");
        Assertions.assertTrue(gameDataEmulator.isCanceledRequests, "Canceled players bets in the round");
        Assertions.assertTrue(gameDataEmulator.isPlayingPlayers, "Connected players in the round");
        Assertions.assertTrue(gameDataEmulator.isWinPlayers, "Winners in the round");
    }

    /**
     * Тест #3
     * *****************************************************
     * В течении времени для 1-раунда создаются 1000-игроков.
     * В течении времени для 1-раунда выполняется всего 1000-запросов на 1000-игроков, но система игры за 1-раунд обработает 1-запрос для каждого игрока (только 1000 с 1000).
     * Всего выполняется 2-раунда игры.
     * В результате каждого раунда можно получить только 1-выиграш, с таким расчетом что все игроки делают разные ставки (в зависимости от времени старта с 2-раундов можно получить 1~2 выиграша).
     */
    @Test
    public void supperStressPlayerEmulation() throws InterruptedException {
        players = 1000; //todo
        requests = 1000; //todo
        long requestFrequency = 1l; //todo
        acceptedRequests = percentRequests(requests, players);
        canceledRequests = 100 - acceptedRequests;

        new Thread(new GamePlayEmulation()).start();

        for (int round=0; round<ROUNDS; round++) {
            new Thread(new GameEngineEmulator(requestFrequency)).start();
            Thread.sleep(TIME_ROUND);
        }

        GameDataEmulator gameDataEmulator = new GameDataEmulator();

        gameDataEmulator.printPlayerReport();

        Assertions.assertTrue(gameDataEmulator.isAcceptedRequests, "Accepted players bets in the round");
        Assertions.assertTrue(gameDataEmulator.isCanceledRequests, "Canceled players bets in the round");
        Assertions.assertTrue(gameDataEmulator.isPlayingPlayers, "Connected players in the round");
        Assertions.assertTrue(gameDataEmulator.isWinPlayers, "Winners in the round");
    }


    private double percentRequests(int allRequests, int requests) {
        return 100 * (double)requests / (double)allRequests;
    }

    private double getProbabilityOfWin(int numberOfPlayers, int numberOfWins) {
        return 100 * (double)numberOfWins / (double)numberOfPlayers;
    }


    private class GameEngineEmulator implements Runnable {
        private final long requestFrequency;

        public GameEngineEmulator(long requestFrequency) {
            this.requestFrequency = requestFrequency;
        }

        @Override
        public void run() {
            for (int request = NUMBER_FROM; request < requests; request++) {
                int player = request % players;
                int number = request % NUMBER_TO;

                try {
                    monitorAllRequests++;
                    prepareGetConnectToGameRound("player" + player, number);
                    monitorCanceledRequests++;
                } catch (Exception ex) {
                    monitorAcceptedRequests++;
                }

                try { Thread.sleep(requestFrequency); } catch (InterruptedException ex) { ex.printStackTrace(); }
            }
        }
    }

    private class GamePlayEmulation implements Runnable {
        @Override
        public void run() {
            for (int i=0; i<100; i++) {
                int playingPlayers = 0;
                int winPlayers = 0;
                int leftPlayers = 0;
                List<Player> players = gameEngineService.findAll()
                        .collectList()
                        .block();

                monitorAllPlayers = monitorAllPlayers < players.size() ? players.size() : monitorAllPlayers;
                for (Player player: players) {
                    if (player.getState().equals(PlayerState.PLAY)) playingPlayers++;
                    if (player.getState().equals(PlayerState.START_ROUND)) leftPlayers++;
                    if (player.getState().equals(PlayerState.STOP_ROUND) && player.getWin()!=null && player.getWin().equals("You win")) winPlayers++;
                }
                monitorPlayingPlayers = monitorPlayingPlayers < playingPlayers ? playingPlayers : monitorPlayingPlayers;
                monitorWinPlayers = monitorWinPlayers < winPlayers ? winPlayers : monitorWinPlayers;
                monitorLeftPlayers = monitorLeftPlayers < leftPlayers ? leftPlayers : monitorLeftPlayers;

                try { Thread.sleep(500l); } catch (InterruptedException ex) { ex.printStackTrace(); }
            }
        }
    }

    private class GameDataEmulator {

        private final String    formatOutDataGeneratedNumber = "| %1$-14s |\n";
        private final String             formatOutDataPlayer = "| %1$-13s | %2$-14s | %3$-16s | %4$-19s | %5$-21s | %6$-12s | %7$-26s | %8$-14s |\n";
        private final String separatorOutDataGeneratedNumber = "+----------------+";
        private final String          separatorOutDataPlayer = "+---------------+----------------+------------------+---------------------+-----------------------+--------------+----------------------------+----------------+";

        private double   _acceptedRequests = percentRequests(monitorAllRequests, monitorAcceptedRequests);
        private double   _canceledRequests = percentRequests(monitorAllRequests, monitorCanceledRequests);
        private double         _winPlayers = getProbabilityOfWin(players, monitorWinPlayers);

        private boolean isMinThrowOfNumber = monitorMinThrowOfNumber==NUMBER_FROM ? true : false;
        private boolean isMaxThrowOfNumber = monitorMaxThrowOfNumber==(NUMBER_TO-1) ? true : false;
        private boolean    isThrowOfNumber = isMinThrowOfNumber && isMaxThrowOfNumber ? true : false;
        private boolean       isAllPlayers = monitorAllPlayers==players ? true : false;
        private boolean   isPlayingPlayers = monitorPlayingPlayers==players ? true : false;
        private boolean       isWinPlayers = WIN_MIN <= _winPlayers && _winPlayers <= WIN_MAX ? true : false;
        private boolean      isLeftPlayers = monitorLeftPlayers==players ? true : false;
        private boolean isAcceptedRequests = _acceptedRequests==acceptedRequests ? true : false;
        private boolean isCanceledRequests = _canceledRequests==canceledRequests ? true : false;

        public void printGeneratedNumberReport() {
            System.out.println(separatorOutDataGeneratedNumber
                    + "\n| ЗАГАДАТЬ ЧИСЛО |"
                    + "\n" + separatorOutDataGeneratedNumber);
            System.out.format(formatOutDataGeneratedNumber, new String[]{monitorMinThrowOfNumber + ".." + monitorMaxThrowOfNumber + " {" + NUMBER_FROM + ".." + (NUMBER_TO-1) + "}"});
            System.out.println(separatorOutDataGeneratedNumber);
            System.out.format(formatOutDataGeneratedNumber, new Boolean[]{isThrowOfNumber});
            System.out.println(separatorOutDataGeneratedNumber);
        }

        public void printPlayerReport() {
            System.out.println(separatorOutDataPlayer
                    + "\n| ВСЕГО РАУНДОВ | ВСЕГО ЗАПРОСОВ | ПРИНЯТЫЕ ЗАПРОСЫ | ОТКЛОНЕННЫЕ ЗАПРОСЫ | ПРИСОЕДИНИЛОСЬ К ИГРЕ | НАЧАЛИ РАУНД |       ВЫИГРАЛИ РАУНД       | ПОКИНУЛИ РАУНД |"
                    + "\n" + separatorOutDataPlayer);
            System.out.format(formatOutDataPlayer,
                    new String[]{ROUNDS + "",
                            monitorAllRequests + "",
                            new DecimalFormat("#0.0").format(_acceptedRequests) + "% {" + new DecimalFormat("#0.0").format(acceptedRequests) + "%}",
                            new DecimalFormat("#0.0").format(_canceledRequests) + "% {" + new DecimalFormat("#0.0").format(canceledRequests) + "%}",
                            monitorAllPlayers + " (" + players + ")",
                            monitorPlayingPlayers + " (" + players + ")",
                            new DecimalFormat("#0.0").format( _winPlayers ) + "% {" + monitorWinPlayers + " of " + new DecimalFormat("#0.0").format(WIN_MIN) + ".." + new DecimalFormat("#0.0").format(WIN_MAX) + "%}",
                            monitorLeftPlayers + " (" + players + ")",
                    });
            System.out.println(separatorOutDataPlayer);
            System.out.format(formatOutDataPlayer, new Boolean[]{null, null, isAcceptedRequests, isCanceledRequests, isAllPlayers, isPlayingPlayers, isWinPlayers, isLeftPlayers});
            System.out.println(separatorOutDataPlayer);
        }
    }
}