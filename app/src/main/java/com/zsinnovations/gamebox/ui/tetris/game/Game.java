package com.zsinnovations.gamebox.ui.tetris.game;

import com.zsinnovations.gamebox.ui.tetris.activity.Tetris_MainActivity;
import com.zsinnovations.gamebox.ui.tetris.blocks.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Game {
    private Shape currentBlock = null;
    private volatile boolean isPaused = false;
    private Timer dropTimer;
    private TimerTask currentTask = null;
    private List<GameObserver> observers = new ArrayList<>();
    private int initialLevel = 1;
    private volatile int totalClearedLines = 0;
    private volatile int level = 1;
    private volatile int score = 0;
    private volatile AtomicInteger blockStatus = new AtomicInteger(0);
    private volatile AtomicIntegerArray leftTop = new AtomicIntegerArray(2);
    private Thread rightThread;
    private Thread leftThread;
    private Thread dropThread;
    private Thread fastDropThread;
    private boolean levelUp = false;
    private boolean rightThreadStarted;
    private boolean leftThreadStarted;
    private boolean dropThreadStarted;
    private boolean fastDropThreadStarted;

    public static Game game = new Game();

    private Game() {
        initializeThreads();
    }

    private void initializeThreads() {
        rightThread = new Thread(() -> {
            leftTop = Board.getBoard().moveBlockRight(currentBlock, leftTop, blockStatus);
            notifyObserversUpdate();
        });
        leftThread = new Thread(() -> {
            leftTop = Board.getBoard().moveBlockLeft(currentBlock, leftTop, blockStatus);
            notifyObserversUpdate();
        });
        dropThread = new Thread(() -> {
            leftTop = Board.getBoard().dropBlock(currentBlock, leftTop, blockStatus);
            notifyObserversUpdate();
        });
        fastDropThread = new Thread(() -> {
            leftTop = Board.getBoard().fastDropBlock(currentBlock, leftTop, blockStatus);
            notifyObserversUpdate();
        });
    }

    public static Game getGame() {
        return game;
    }

    public void setInitialLevel(int initialLevel) {
        this.initialLevel = initialLevel;
        level = initialLevel;
    }

    public synchronized void pause() {
        if (!isPaused) {
            isPaused = true;
            if (dropTimer != null) {
                dropTimer.cancel();
                dropTimer = null;
            }
            interruptAllThreads();
        }
    }

    private void interruptAllThreads() {
        if (rightThread != null) rightThread.interrupt();
        if (leftThread != null) leftThread.interrupt();
        if (dropThread != null) dropThread.interrupt();
        if (fastDropThread != null) fastDropThread.interrupt();
    }

    public synchronized void resume() {
        if (isPaused) {
            isPaused = false;
            initializeThreads();
            resetThreadFlags();
            scheduleTimer();
        }
    }

    private void resetThreadFlags() {
        rightThreadStarted = false;
        leftThreadStarted = false;
        dropThreadStarted = false;
        fastDropThreadStarted = false;
    }

    public void start() {
        isPaused = false;
        notifyObserversClear(0, 0, initialLevel);
        scheduleTimer();
    }

    private void scheduleTimer() {
        if (isPaused) return;

        if (dropTimer != null) {
            dropTimer.cancel();
        }

        dropTimer = new Timer();
        int period = (20 - level) * 50;

        currentTask = new TimerTask() {
            @Override
            public void run() {
                if (isPaused) return;

                if (levelUp) {
                    dropTimer.cancel();
                    levelUp = false;
                    scheduleTimer();
                    return;
                }

                if (currentBlock == null) {
                    leftTop = generateNewBlock();
                    blockStatus.set(0);
                }

                if (leftTop.get(0) == -100) {
                    notifyObserversEnd(score);
                    dropTimer.cancel();
                    return;
                }

                leftTop = dropBlock();
                if (leftTop.get(0) == -10) {
                    currentBlock = null;
                }

                notifyObserversUpdate();
            }
        };

        dropTimer.scheduleAtFixedRate(currentTask, period / 3, period);
    }

    private void updateGameInfo(int clearedLines) {
        if (clearedLines == 0) {
            return;
        }
        totalClearedLines += clearedLines;
        score += ScoreCounter.getScoreCounter().lineToScore(clearedLines);
        int newLevel = LevelCounter.getLevelCounter().lineToLevel(totalClearedLines);
        if (initialLevel + newLevel > level) {
            levelUp = true;
        }
        level = initialLevel + newLevel;
        notifyObserversClear(totalClearedLines, score, level);
    }

    public void end() {
        isPaused = false;
        if (dropTimer != null) {
            dropTimer.cancel();
            dropTimer = null;
        }

        levelUp = false;
        totalClearedLines = 0;
        level = initialLevel;
        score = 0;
        currentBlock = null;

        interruptAllThreads();
        Board.getBoard().clear();
        resetThreadFlags();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int[][] getCurrentBoardMatrix() {
        return Board.getBoard().getBoardMatrix();
    }

    private synchronized AtomicIntegerArray generateNewBlock() {
        updateGameInfo(Board.getBoard().getClearedLines());
        currentBlock = BlockGenerator.getBlockGenerator().generateBlock();
        notifyObserversNew(BlockGenerator.getBlockGenerator().getNextBlock().getShape());
        return Board.getBoard().addBlock(currentBlock);
    }

    private synchronized AtomicIntegerArray dropBlock() {
        return Board.getBoard().dropBlock(currentBlock, leftTop, blockStatus);
    }

    public synchronized void rotateBlock() {
        if (leftTop.get(0) == 0 || leftTop.get(0) == -1 || leftTop.get(0) > 8) {
            return;
        }

        Board.getBoard().rotateBlock(currentBlock, leftTop, blockStatus);
        if (blockStatus.get() == 3) {
            blockStatus.set(0);
        } else {
            blockStatus.getAndIncrement();
        }
        notifyObserversUpdate();
    }

    public synchronized void moveBlockLeft() {
        if (leftTop.get(0) < -2) {
            return;
        }
        if (!leftThreadStarted) {
            leftThread.start();
            leftThreadStarted = true;
        } else {
            leftThread.run();
        }
    }

    public synchronized void moveBlockRight() {
        if (leftTop.get(0) < -2) {
            return;
        }
        if (!rightThreadStarted) {
            rightThread.start();
            rightThreadStarted = true;
        } else {
            rightThread.run();
        }
    }

    public synchronized void moveBlockDown() {
        if (leftTop.get(0) < -2 || leftTop.get(1) > 15) {
            return;
        }
        if (!dropThreadStarted) {
            dropThread.start();
            dropThreadStarted = true;
        } else {
            dropThread.run();
        }
    }

    public synchronized void moveBlockDownFast() {
        if (leftTop.get(0) < -2 || leftTop.get(1) > 15) {
            return;
        }
        if (!fastDropThreadStarted) {
            fastDropThread.start();
            fastDropThreadStarted = true;
        } else {
            fastDropThread.run();
        }
    }

    public void attach(GameObserver observer) {
        observers.add(observer);
    }

    public void detach(GameObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObserversUpdate() {
        for (GameObserver observer : observers) {
            if (observer instanceof Tetris_MainActivity) {
                ((Tetris_MainActivity) observer).runOnUiThread(() -> {
                    observer.updateCanvas();
                });
            } else {
                observer.updateCanvas();
            }
        }
    }

    protected void notifyObserversNew(int[][] shapeMatrix) {
        for (GameObserver observer : observers) {
            if (shapeMatrix.length == 4) {
                observer.generateNewBlock(shapeMatrix);
                return;
            }
            int[][] matrix = new int[4][4];
            for (int i = 0; i < shapeMatrix.length; i++) {
                System.arraycopy(shapeMatrix[i], 0, matrix[i + 1], 1, shapeMatrix.length);
            }
            observer.generateNewBlock(matrix);
        }
    }

    protected void notifyObserversClear(int totalCleardLines, int score, int level) {
        for (GameObserver observer : observers) {
            if (observer instanceof Tetris_MainActivity) {
                ((Tetris_MainActivity) observer).runOnUiThread(() -> {
                    observer.updateGameInfo(totalCleardLines, score, level);
                });
            } else {
                observer.updateGameInfo(totalCleardLines, score, level);
            }
        }
    }

    protected void notifyObserversEnd(int finalScore) {
        for (GameObserver observer : observers) {
            if (observer instanceof Tetris_MainActivity) {
                ((Tetris_MainActivity) observer).runOnUiThread(() -> {
                    observer.gameEnd(finalScore);
                });
            } else {
                observer.gameEnd(finalScore);
            }
        }
    }

    public AtomicIntegerArray getLeftTop() {
        return leftTop;
    }

    public Shape getCurrentBlock() {
        return currentBlock;
    }
}