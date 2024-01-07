package hu.nye.wumpusgame.service.game;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

import hu.nye.wumpusgame.model.CellType;
import hu.nye.wumpusgame.model.Direction;
import hu.nye.wumpusgame.model.GameBoardVO;
import hu.nye.wumpusgame.model.Hero;
import hu.nye.wumpusgame.service.persistence.GameBoardEntity;
import hu.nye.wumpusgame.service.persistence.GameBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;

@Component
public class Game {
    @Autowired
    private GameBoardRepository gameBoardRepository;
    private GameBoardVO gameBoardVO;
    private Hero hero;
    static int wumpusCount = 0;
    public static int arrows = wumpusCount;
    boolean hasGold = false;


    public Game(GameBoardRepository gameBoardRepository) {
        this.gameBoardRepository = gameBoardRepository;
    }

    public void setGameBoardRepository(GameBoardRepository gameBoardRepository) {
        this.gameBoardRepository = gameBoardRepository;
    }

    public Game(int arrows) {
        this.arrows = arrows;
    }

    static CellType[][] cells;


    public Game(GameBoardVO gameBoardVO) {
    }

    public void play() throws IOException, SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            printMenu();
            System.out.print("Válassz egy lehetőséget: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    editBoard();
                    break;
                case 2:
                    loadFromFile();
                    break;
                case 3:
                    loadFromDatabase();
                    break;
                case 4:
                    savetoDatabase();
                    break;
                case 5:
                    playGame();
                    break;
                case 6:
                    System.out.println("Kilépés a játékból. Viszlát!");
                    break;
                default:
                    System.out.println("Kérlek válassz másik lehetőséget.");
                    break;
            }

        } while (choice != 6);
    }

    private void savetoDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "Bogyesz2022");
        // Először konvertáljuk a jelenlegi pályaállapotot GameBoardEntity típussá
        GameBoardEntity gameBoardEntity = convertToGameBoardEntity();
        GameBoardRepository gameBoardRepository = new GameBoardRepository() {
            @Override
            public List<GameBoardEntity> findAll(Sort sort) {
                return null;
            }

            @Override
            public Page<GameBoardEntity> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends GameBoardEntity> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends GameBoardEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<GameBoardEntity> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public GameBoardEntity getOne(Long aLong) {
                return null;
            }

            @Override
            public GameBoardEntity getById(Long aLong) {
                return null;
            }

            @Override
            public GameBoardEntity getReferenceById(Long aLong) {
                return null;
            }

            @Override
            public <S extends GameBoardEntity> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends GameBoardEntity> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends GameBoardEntity> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public <S extends GameBoardEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends GameBoardEntity> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends GameBoardEntity> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public <S extends GameBoardEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                return null;
            }

            @Override
            public <S extends GameBoardEntity> S save(S entity) {
                return null;
            }

            @Override
            public <S extends GameBoardEntity> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public Optional<GameBoardEntity> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public List<GameBoardEntity> findAll() {
                return null;
            }

            @Override
            public List<GameBoardEntity> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(GameBoardEntity entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends GameBoardEntity> entities) {

            }

            @Override
            public void deleteAll() {

            }
        }; // itt a megfelelő inicializálási logika lehet
        Game game = new Game(gameBoardRepository);

        // Mentjük az adatbázisba
        gameBoardRepository.save(gameBoardEntity);
    }

    // Ezt a metódust a jelenlegi pályaállapot GameBoardEntity-re való konvertálására használjuk
    private static GameBoardEntity convertToGameBoardEntity() {
        GameBoardEntity gameBoardEntity = new GameBoardEntity();
        gameBoardEntity.setId(GameBoardEntity.getId());
        gameBoardEntity.setUsername(GameBoardEntity.getUsername());
        gameBoardEntity.setSize(GameBoardEntity.getSize());
        gameBoardEntity.setBoardData(GameBoardEntity.getBoardData());

        return gameBoardEntity;
    }

    void editBoard() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Pályaszerkesztés...");

        System.out.print("Adja meg a pálya méretét (NxN, 6<=N<=20): ");
        int size = scanner.nextInt();
        scanner.nextLine();

        if (size < 6 || size > 20) {
            System.out.println("Érvénytelen méret. Kérlek, próbáld újra.");
            return;
        }

        System.out.print("Adja meg a hős oszlopát: ");
        final int col = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Adja meg a hős sorát: ");
        int row = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Adja meg a hős irányát (NORTH/WEST/SOUTH/EAST): ");
        Direction direction = Direction.valueOf(scanner.nextLine().toUpperCase());
        CellType[][] cells = new CellType[size][size];

        this.hero = new Hero(col, row, direction, gameBoardVO);
        this.gameBoardVO = new GameBoardVO();


        if (size <= 8) {
            wumpusCount = 1;
        } else if (size >= 9 && size <= 14) {
            wumpusCount = 2;
        } else if (size >= 15) {
            wumpusCount = 3;
        }

    }

    public int getArrows() {
        return arrows;
    }

    private void loadFromFile() {

        ;
        arrows = 1;
        Direction direction = Direction.EAST;
        System.out.println("Hős iránya: " + direction);
        System.out.println("Nyilak száma: " + arrows);
    }

    private void printMenu() {
        System.out.println("===== Játék Menü =====");
        System.out.println("1. Pályaszerkesztés");
        System.out.println("2. Fileból betöltés");
        System.out.println("3. Adatbázisból betöltés");
        System.out.println("4. Adatbázisba mentés");
        System.out.println("5. Játék");
        System.out.println("6. Kilépés");
    }

    void playGame() {
        System.out.println("\nJáték indítása...");
        System.out.println("\nSzerezd meg az aranyat és menj vissza vele a kezdőpozícióba!");

        Scanner scanner = new Scanner(System.in);

        int steps = 0;


        while (true) {
            GameBoardVO.printBoard();

            System.out.println("\n--- Akciók ---");
            System.out.println("1. Lépés");
            System.out.println("2. Jobbra fordul");
            System.out.println("3. Balra fordul");
            System.out.println("4. Lövés");
            System.out.println("5. Arany felvétele");
            System.out.println("6. Kilépés a főmenübe");

            System.out.print("Válaszd ki az akciót (1-6): ");
            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1 -> {
                    hero.move();
                    steps++;
                    GameBoardVO.printBoard();
                    System.out.println("Hős iránya: " + Hero.getDirection());
                    System.out.println("Nyilak száma: " + arrows);
                }
                case 2 -> {
                    hero.turnRight();
                    GameBoardVO.printBoard();
                    System.out.println("Hős iránya: " + Hero.getDirection());
                    System.out.println("Nyilak száma: " + arrows);
                }
                case 3 -> {
                    hero.turnLeft();
                    GameBoardVO.printBoard();
                    System.out.println("Hős iránya: " + Hero.getDirection());
                    System.out.println("Nyilak száma: " + arrows);
                }
                case 4 -> {
                    hero.shoot();
                    GameBoardVO.printBoard();
                    System.out.println("Hős iránya: " + Hero.getDirection());
                    System.out.println("Nyilak száma: " + arrows);
                }
                case 5 -> {
                    hero.pickupGold();
                    GameBoardVO.printBoard();
                    System.out.println("Hős iránya: " + Hero.getDirection());
                    System.out.println("Nyilak száma: " + arrows);
                }
                case 6 -> {
                    System.out.println("Feladás...");
                    return;
                }
                default -> System.out.println("Érvénytelen választás. Kérlek, válassz újra.");
            }

            //      if (isGameOver() {
            //          playing = false;
            //          System.out.println("Játék vége!");
            //      }
        }
    }

    private static boolean isGameOver(String userName, int steps) {
        return false;
    }

    static void loadFromDatabase() {
        System.out.println("Ez még fejlesztés alatt van. Kérlek válassz másik menüpontot.");
    }



}





