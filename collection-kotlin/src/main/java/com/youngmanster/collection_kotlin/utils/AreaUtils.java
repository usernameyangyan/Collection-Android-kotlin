package com.youngmanster.collection_kotlin.utils;

import android.graphics.Point;

import java.util.List;

import static java.lang.Math.sqrt;

/**
 * author : yangyan
 * date : 2021/6/24 7:00 PM
 * description :
 */
public class AreaUtils {

    /**
     * @param point   指定的某个点
     * @param APoints 多边形的各个顶点坐标（首末点可以不一致）
     * @return
     */
    public static boolean ptInPolygon(Point point, List<Point> APoints) {
        int nCross = 0;
        for (int i = 0; i < APoints.size(); i++) {
            Point p1 = APoints.get(i);
            Point p2 = APoints.get((i + 1) % APoints.size());
            // 求解 y=p.y 与 p1p2 的交点
            if (p1.x == p2.y) // p1p2 与 y=p0.y平行
                continue;
            if (point.y < Math.min(p1.y, p2.y)) // 交点在p1p2延长线上
                continue;
            if (point.y >= Math.max(p1.y, p2.y)) // 交点在p1p2延长线上
                continue;
            double x = (double) (point.y - p1.y)
                    * (double) (p2.x - p1.x)
                    / (double) (p2.y - p1.y) + p1.x;
            if (x > point.x)
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }

    public static boolean ptInLine(Point point, List<Point> APoints, int nNear) {

        try {
            for (int i = 0; i < APoints.size(); i++) {
                if (DistPt2Line(APoints.get(i).x, APoints.get(i).y, APoints.get(i + 1).x, APoints.get(i + 1).y, point.x, point.y) < (double) nNear) {
                    return true;
                }
            }
        } catch (Exception e) {
        }

        return false;
    }


    private static double DistPt2Line(double lx1, double ly1, double lx2, double ly2, double px, double py) {
        double abx = lx2 - lx1;
        double aby = ly2 - ly1;
        double acx = px - lx1;
        double acy = py - ly1;
        double f = abx * acx + aby * acy;
        if (f < 0) return Pt2Pt(lx1, ly1, px, py);

        double d = abx * abx + aby * aby;
        if (f > d) return Pt2Pt(lx2, ly2, px, py);

        //   D   =   a   +   [(ab*ac)/(ab*ab)]*ab
        f /= d;
        double dx = lx1 + f * abx;
        double dy = ly1 + f * aby;
        return Pt2Pt(dx, dy, px, py);
    }


    private static double Pt2Pt(double x1, double y1, double x2, double y2) {
        return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

    }


    /**
     * 两【线段】是否相交
     *
     * @return 是否相交
     */
    public static boolean doubleLineIntersection(Point startPoint, Point endPoint,
                                                 Point startPoint1, Point endPoint1) {
        // 快速排斥实验 首先判断两条线段在 x 以及 y 坐标的投影是否有重合。 有一个为真，则代表两线段必不可交。
        if (Math.max(startPoint.x, endPoint.x) < Math.min(startPoint1.x, endPoint1.x)
                || Math.max(startPoint.y, endPoint.y) < Math.min(startPoint1.y, endPoint1.y)
                || Math.max(startPoint1.x, endPoint1.x) < Math.min(startPoint.x, endPoint.x)
                || Math.max(startPoint1.y, endPoint1.y) < Math.min(startPoint.y, endPoint.y)) {
            return false;
        }
        // 跨立实验  如果相交则矢量叉积异号或为零，大于零则不相交
        if ((((startPoint.x - startPoint1.x) * (endPoint1.y - startPoint1.y) - (startPoint.y - startPoint1.y) * (endPoint1.x - startPoint1.x))
                * ((endPoint.x - startPoint1.x) * (endPoint1.y - startPoint1.y) - (endPoint.y - startPoint1.y) * (endPoint1.x - startPoint1.x))) > 0
                || (((startPoint1.x - startPoint.x) * (endPoint.y - startPoint.y) - (startPoint1.y - startPoint.y) * (endPoint.x - startPoint.x))
                * ((endPoint1.x - startPoint.x) * (endPoint.y - startPoint.y) - (endPoint1.y - startPoint.y) * (endPoint.x - startPoint.x))) > 0) {
            return false;
        }
        return true;
    }


}
