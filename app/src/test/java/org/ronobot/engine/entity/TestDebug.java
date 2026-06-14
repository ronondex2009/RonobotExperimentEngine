import org.ronobot.engine.entity.EnemyEntity;
import org.ronobot.engine.math.Position;
import org.ronobot.engine.math.Size;

public class TestDebug {
    public static void main(String[] args) {
        Position TEST_POSITION = new Position(100f, 100f);
        
        EnemyEntity enemy = new EnemyEntity(1, TEST_POSITION.getX(), TEST_POSITION.getY(), "Test");
        System.out.println("Initial health: " + enemy.getHealth());
        System.out.println("Initial active: " + enemy.isActive());
        
        enemy.takeDamage(200);
        
        System.out.println("After takeDamage(200):");
        System.out.println("Health: " + enemy.getHealth());
        System.out.println("Active: " + enemy.isActive());
    }
}
