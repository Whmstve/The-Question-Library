package net.whmstve.thequestionlib.general.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class Transform implements Position, Comparable<Transform> {
    private final double x;
    private final double y;
    private final double z;

    public static final Transform ZERO = new Transform(0,0,0);

    public Transform(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    public static Transform fromPosition(@NotNull Vec3 position){
        return new Transform(position.x(), position.y(), position.z());
    }

    public static Transform fromPosition(@NotNull Vec3i position){
        return new Transform(position.getX(), position.getY(), position.getZ());
    }

    public static Transform combine(@NotNull Position a, @NotNull Position b){
        return new Transform(a.x()+b.x(),a.y()+b.y(),a.z()+b.z());
    }

    public static Transform split(@NotNull Position a, @NotNull Position b){
        return new Transform(a.x()-b.x(),a.y()-b.y(),a.z()-b.z());
    }

    public static Transform combine(@NotNull Vec3i a, @NotNull Vec3i b){
        return new Transform(a.getX()+b.getX(),a.getY()+b.getY(),a.getZ()+b.getZ());
    }

    public static Transform split(@NotNull Vec3i a, @NotNull Vec3i b){
        return new Transform(a.getX()-b.getX(),a.getY()-b.getY(),a.getZ()-b.getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transform transform = (Transform) o;
        return Double.compare(transform.x, x) == 0 && Double.compare(transform.y, y) == 0 && Double.compare(transform.z, z) == 0;
    }

    public static Transform max(Transform a, Transform b){
        if(a.equals(b)) return a;
        if(a.compareTo(b)<0) return b;
        else return a;
    }

    public static Transform min(Transform a, Transform b){
        if(a.equals(b)) return a;
        if(a.compareTo(b)>0) return b;
        else return a;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public List<Transform> draw(Transform b) {
        List<Transform> result = new ArrayList<>();
        Transform direction = this.difference(b);
        double length = Math.sqrt(this.distanceToSqr(b));
        double step = 0.2; // Customize as needed
        Transform unitStep = direction.normalize().scale(step);

        Transform current = this;
        for (double dist = 0; dist <= length; dist += step) {
            result.add(current);
            current = current.add(unitStep);
        }

        return result;
    }


    public Transform add(Transform transform){
        return new Transform(this.x+transform.x,this.y+transform.y, this.z+transform.z);
    }

    public Transform add(double value){
        return add(value,value,value);
    }

    public Transform add(double valueHorizontal, double valueY){
        return add(valueHorizontal,valueY,valueHorizontal);
    }

    public Transform add(double valueX, double valueY, double valueZ){
        return new Transform(this.x+valueX,this.y+valueY,this.z+valueZ);
    }

    public Transform subtract(Transform transform){
        return new Transform(this.x-transform.x,this.y-transform.y, this.z-transform.z);
    }

    public Transform subtract(double value){
        return subtract(value,value,value);
    }

    public Transform subtract(double valueHorizontal, double valueY){
        return subtract(valueHorizontal,valueY,valueHorizontal);
    }

    public Transform subtract(double valueX, double valueY, double valueZ){
        return new Transform(this.x-valueX,this.y-valueY,this.z-valueZ);
    }

    public Transform multiply(Transform transform){
        return new Transform(this.x*transform.x,this.y*transform.y, this.z*transform.z);
    }

    public Transform multiply(double value){
        return multiply(value,value,value);
    }

    public Transform multiply(double valueHorizontal, double valueY){
        return multiply(valueHorizontal,valueY,valueHorizontal);
    }

    public Transform multiply(double valueX, double valueY, double valueZ){
        return new Transform(this.x*valueX,this.y*valueY,this.z*valueZ);
    }

    public Transform multiply(Matrix3f mat){
        return new Transform(
                Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z))
        );
    }

    public Transform multiply(Matrix3fc mat){
        return new Transform(
                    Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                    Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                    Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z))
                );
    }

    public Transform multiply(Matrix3dc mat){
        return new Transform(
                Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z))
        );
    }

    public Transform multiply(Matrix3x2fc mat){
        return new Transform(
                Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                z
        );
    }

    public Transform divide(Transform transform){
        return new Transform(this.x/transform.x,this.y/transform.y, this.z/transform.z);
    }

    public Transform divide(double value){
        return divide(value,value,value);
    }

    public Transform divide(double valueHorizontal, double valueY){
        return divide(valueHorizontal,valueY,valueHorizontal);
    }

    public Transform divide(double valueX, double valueY, double valueZ){
        return new Transform(this.x/valueX,this.y/valueY,this.z/valueZ);
    }

    public Transform scale(double factor){
        return scale(factor,factor,factor);
    }

    public Transform scale(double factorX, double factorY, double factorZ){
        return new Transform(this.x*factorX, this.y*factorY, this.z*factorZ);
    }

    /**
     * Normalizes the transform to a length of 1 (except if it is the zero transform)
     */
    public Transform normalize(){
        double value = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return value < 1.0E-4D ? ZERO : new Transform(this.x / value, this.y / value, this.z / value);
    }

    /**
     * Returns a transform representing the distance between this and another transform
     * as a length of 1 (except if the result is the zero transform)
     */
    public Transform difference(Transform other) {
        if(this.equals(other) || (this.equals(Transform.ZERO) ^ other.equals(Transform.ZERO))) return Transform.ZERO;
        return this.subtract(other).normalize();
    }

    public Transform reverse(){
        return this.scale(-1);
    }

    public double distanceTo(Transform other){
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double dz = other.z - this.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double distanceTo(double nx, double ny, double nz){
        double dx = nx - this.x;
        double dy = ny - this.y;
        double dz = nz - this.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double distanceToSqr(Transform other){
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double dz = other.z - this.z;
        return (dx * dx + dy * dy + dz * dz);
    }

    public double distanceToSqr(double nx, double ny, double nz){
        double dx = nx - this.x;
        double dy = ny - this.y;
        double dz = nz - this.z;
        return (dx * dx + dy * dy + dz * dz);
    }

    public Transform lerp(Transform other, double delta){
        return new Transform(Mth.lerp(delta,this.x,other.x), Mth.lerp(delta,this.y,other.y), Mth.lerp(delta,this.z,other.z));
    }

    /**
     * Returns the length of the transform.
     */
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double lengthSqr() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public double horizontalDistance() {
        return Math.sqrt(this.x * this.x + this.z * this.z);
    }

    public double horizontalDistanceSqr() {
        return this.x * this.x + this.z * this.z;
    }

    @Override
    public int compareTo(@NotNull Transform o) {
        int cmp = Double.compare(this.x, o.x);
        if (cmp != 0) return cmp;

        cmp = Double.compare(this.y, o.y);
        if (cmp != 0) return cmp;

        return Double.compare(this.z, o.z);
    }

    public Transform xRot(float pPitch) {
        float f = Mth.cos(pPitch);
        float f1 = Mth.sin(pPitch);
        double d1 = this.y * (double)f + this.z * (double)f1;
        double d2 = this.z * (double)f - this.y * (double)f1;
        return new Transform(this.x, d1, d2);
    }

    public Transform yRot(float pYaw) {
        float f = Mth.cos(pYaw);
        float f1 = Mth.sin(pYaw);
        double d0 = this.x * (double)f + this.z * (double)f1;
        double d2 = this.z * (double)f - this.x * (double)f1;
        return new Transform(d0, this.y, d2);
    }

    public Transform zRot(float pRoll) {
        float f = Mth.cos(pRoll);
        float f1 = Mth.sin(pRoll);
        double d0 = this.x * (double)f + this.y * (double)f1;
        double d1 = this.y * (double)f - this.x * (double)f1;
        return new Transform(d0, d1, this.z);
    }

    /**
     * Returns a {@link Transform} from the given pitch and yaw degrees as {@link Transform}.
     */
    public static Transform directionFromRotation(Vec2 pVec) {
        return directionFromRotation(pVec.x, pVec.y);
    }

    /**
     * Returns a {@link Transform} from the given pitch and yaw degrees.
     */
    public static Transform directionFromRotation(float pPitch, float pYaw) {
        float f = Mth.cos(-pYaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float f1 = Mth.sin(-pYaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float f2 = -Mth.cos(-pPitch * ((float)Math.PI / 180F));
        float f3 = Mth.sin(-pPitch * ((float)Math.PI / 180F));
        return new Transform((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

    public Transform align(EnumSet<Direction.Axis> pAxes) {
        double d0 = pAxes.contains(Direction.Axis.X) ? (double)Mth.floor(this.x) : this.x;
        double d1 = pAxes.contains(Direction.Axis.Y) ? (double)Mth.floor(this.y) : this.y;
        double d2 = pAxes.contains(Direction.Axis.Z) ? (double)Mth.floor(this.z) : this.z;
        return new Transform(d0, d1, d2);
    }

    public double get(Direction.Axis pAxis) {
        return pAxis.choose(this.x, this.y, this.z);
    }

    public Transform with(Direction.Axis pAxis, double pLength) {
        double d0 = pAxis == Direction.Axis.X ? pLength : this.x;
        double d1 = pAxis == Direction.Axis.Y ? pLength : this.y;
        double d2 = pAxis == Direction.Axis.Z ? pLength : this.z;
        return new Transform(d0, d1, d2);
    }

    public Transform relative(Direction pDirection, double pLength) {
        Vec3i vec3i = pDirection.getNormal();
        return new Transform(this.x + pLength * (double)vec3i.getX(), this.y + pLength * (double)vec3i.getY(), this.z + pLength * (double)vec3i.getZ());
    }

    public Transform offsetRandom(RandomSource pRandom, float pFactor) {
        return this.add(new Transform((pRandom.nextFloat() - 0.5F) * pFactor, (pRandom.nextFloat() - 0.5F) * pFactor, (pRandom.nextFloat() - 0.5F) * pFactor));
    }

    public boolean closerThan(Position pPos, double pDistance) {
        return this.distanceToSqr(pPos.x(), pPos.y(), pPos.z()) < pDistance * pDistance;
    }

    /**
     * Returns a new transform with the result of this transform x the specified transform.
     */
    public Transform cross(Transform transform) {
        return new Transform(this.y * transform.z - this.z * transform.y, this.z * transform.x - this.x * transform.z, this.x * transform.y - this.y * transform.x);
    }

    public BlockPos blockPos(){
        return new BlockPos((int) this.x, (int) this.y, (int) this.z);
    }

    public Vec3 vector(){
        return new Vec3(this.x, this.y, this.z);
    }

    public Vec3i intVector(){
        return new Vec3i((int) this.x, (int) this.y, (int) this.z);
    }

    public FloatTransform toFloat(){
        return new FloatTransform((float) this.x, (float) this.y, (float) this.z);
    }

    public IntTransform toInt(){
        return new IntTransform((int) this.x, (int) this.y, (int) this.z);
    }
}
