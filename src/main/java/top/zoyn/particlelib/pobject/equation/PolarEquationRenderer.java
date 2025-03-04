package top.zoyn.particlelib.pobject.equation;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import top.zoyn.particlelib.ParticleLib;
import top.zoyn.particlelib.pobject.ParticleObject;
import top.zoyn.particlelib.pobject.Playable;

import java.util.List;
import java.util.function.Function;

/**
 * 表示一个极坐标方程渲染器
 *
 * @author Zoyn
 */
public class PolarEquationRenderer extends ParticleObject implements Playable {

    private final Function<Double, Double> function;
    private double minTheta;
    private double maxTheta;
    private double dTheta;
    private double currentTheta;

    /**
     * 极坐标渲染器
     *
     * @param origin   原点
     * @param function 极坐标方程
     */
    public PolarEquationRenderer(Location origin, Function<Double, Double> function) {
        this(origin, function, 0D, 360D, 1D);
    }

    /**
     * 极坐标渲染器
     *
     * @param origin   原点
     * @param function 极坐标方程
     * @param minTheta 自变量最小值
     * @param maxTheta 自变量最大值
     * @param dTheta   每次自变量所增加的量
     */
    public PolarEquationRenderer(Location origin, Function<Double, Double> function, double minTheta, double maxTheta, double dTheta) {
        setOrigin(origin);
        this.function = function;
        this.minTheta = minTheta;
        this.maxTheta = maxTheta;
        this.dTheta = dTheta;
    }

    @Override
    public List<Location> calculateLocations() {
        List<Location> points = Lists.newArrayList();
        for (double theta = minTheta; theta < maxTheta; theta += dTheta) {
            double rho = function.apply(theta);
            double x = rho * Math.cos(theta);
            double y = rho * Math.sin(theta);
            points.add(getOrigin().clone().add(x, y, 0));
        }
        return points;
    }

    @Override
    public void show() {
        for (double theta = minTheta; theta < maxTheta; theta += dTheta) {
            double rho = function.apply(theta);
            double x = rho * Math.cos(theta);
            double y = rho * Math.sin(theta);
            spawnParticle(getOrigin().clone().add(x, y, 0));
        }
    }

    @Override
    public void play() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // 进行关闭
                if (currentTheta > maxTheta) {
                    cancel();
                    return;
                }
                currentTheta += dTheta;

                double rho = function.apply(currentTheta);
                double x = rho * Math.cos(currentTheta);
                double y = rho * Math.sin(currentTheta);
                spawnParticle(getOrigin().clone().add(x, y, 0));
            }
        }.runTaskTimer(ParticleLib.getInstance(), 0, getPeriod());
    }

    @Override
    public void playNextPoint() {
        // 进行关闭
        if (currentTheta > maxTheta) {
            currentTheta = minTheta;
        }
        currentTheta += dTheta;

        double rho = function.apply(currentTheta);
        double x = rho * Math.cos(currentTheta);
        double y = rho * Math.sin(currentTheta);
        spawnParticle(getOrigin().clone().add(x, y, 0));
    }

    public double getMinTheta() {
        return minTheta;
    }

    public PolarEquationRenderer setMinTheta(double minTheta) {
        this.minTheta = minTheta;
        return this;
    }

    public double getMaxTheta() {
        return maxTheta;
    }

    public PolarEquationRenderer setMaxTheta(double maxTheta) {
        this.maxTheta = maxTheta;
        return this;
    }

    public double getDTheta() {
        return dTheta;
    }

    public PolarEquationRenderer setDTheta(double dTheta) {
        this.dTheta = dTheta;
        return this;
    }
}
