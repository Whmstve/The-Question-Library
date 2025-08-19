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

public class FloatTransform implements Position, Comparable<FloatTransform> {
    private final float x;
    private final float y;
    private final float z;

    public static final FloatTransform ZERO = new FloatTransform(0,0,0);

    public FloatTransform(float x, float y, float z){
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

    public static FloatTransform fromPosition(@NotNull Vec3 position){
        return new FloatTransform((float) position.x(), (float) position.y(), (float) position.z());
    }

    public static FloatTransform fromPosition(@NotNull Vec3i position){
        return new FloatTransform(position.getX(), position.getY(), position.getZ());
    }

    public static FloatTransform combine(@NotNull Position a, @NotNull Position b){
        return new FloatTransform((float) (a.x()+b.x()), (float) (a.y()+b.y()), (float) (a.z()+b.z()));
    }

    public static FloatTransform split(@NotNull Position a, @NotNull Position b){
        return new FloatTransform((float) (a.x()-b.x()), (float) (a.y()-b.y()), (float) (a.z()-b.z()));
    }

    public static FloatTransform combine(@NotNull Vec3i a, @NotNull Vec3i b){
        return new FloatTransform(a.getX()+b.getX(),a.getY()+b.getY(),a.getZ()+b.getZ());
    }

    public static FloatTransform split(@NotNull Vec3i a, @NotNull Vec3i b){
        return new FloatTransform(a.getX()-b.getX(),a.getY()-b.getY(),a.getZ()-b.getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatTransform transform = (FloatTransform) o;
        return Double.compare(transform.x, x) == 0 && Double.compare(transform.y, y) == 0 && Double.compare(transform.z, z) == 0;
    }

    public static FloatTransform max(FloatTransform a, FloatTransform b){
        if(a.equals(b)) return a;
        if(a.compareTo(b)<0) return b;
        else return a;
    }

    public static FloatTransform min(FloatTransform a, FloatTransform b){
        if(a.equals(b)) return a;
        if(a.compareTo(b)>0) return b;
        else return a;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public List<FloatTransform> draw(FloatTransform b) {
        List<FloatTransform> result = new ArrayList<>();
        FloatTransform direction = this.difference(b);
        double length = Math.sqrt(this.distanceToSqr(b));
        double step = 0.2; // Customize as needed
        FloatTransform unitStep = direction.normalize().scale(step);

        FloatTransform current = this;
        for (double dist = 0; dist <= length; dist += step) {
            result.add(current);
            current = current.add(unitStep);
        }

        return result;
    }

    public FloatTransform add(FloatTransform transform){
        return new FloatTransform(this.x+transform.x,this.y+transform.y, this.z+transform.z);
    }

    public FloatTransform add(float value){
        return add(value,value,value);
    }

    public FloatTransform add(float valueHorizontal, float valueY){
        return add(valueHorizontal,valueY,valueHorizontal);
    }

    public FloatTransform add(float valueX, float valueY, float valueZ){
        return new FloatTransform((float) (this.x+valueX), (float) (this.y+valueY), (float) (this.z+valueZ));
    }

    public FloatTransform subtract(FloatTransform transform){
        return new FloatTransform(this.x-transform.x,this.y-transform.y, this.z-transform.z);
    }

    public FloatTransform subtract(double value){
        return subtract(value,value,value);
    }

    public FloatTransform subtract(double valueHorizontal, double valueY){
        return subtract(valueHorizontal,valueY,valueHorizontal);
    }

    public FloatTransform subtract(double valueX, double valueY, double valueZ){
        return new FloatTransform((float) (this.x-valueX), (float) (this.y-valueY), (float) (this.z-valueZ));
    }

    public FloatTransform multiply(FloatTransform transform){
        return new FloatTransform(this.x*transform.x,this.y*transform.y, this.z*transform.z);
    }

    public FloatTransform multiply(double value){
        return multiply(value,value,value);
    }

    public FloatTransform multiply(double valueHorizontal, double valueY){
        return multiply(valueHorizontal,valueY,valueHorizontal);
    }

    public FloatTransform multiply(double valueX, double valueY, double valueZ){
        return new FloatTransform((float) (this.x*valueX), (float) (this.y*valueY), (float) (this.z*valueZ));
    }

    public FloatTransform multiply(Matrix3f mat){
        return new FloatTransform(
                Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z))
        );
    }

    public FloatTransform multiply(Matrix3fc mat){
        return new FloatTransform(
                Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z))
        );
    }

    public FloatTransform multiply(Matrix3dc mat){
        return new FloatTransform(
                (float) Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                (float) Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                (float) Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z))
        );
    }

    public FloatTransform multiply(Matrix3x2fc mat){
        return new FloatTransform(
                Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                z
        );
    }

    public FloatTransform divide(FloatTransform transform){
        return new FloatTransform(this.x/transform.x,this.y/transform.y, this.z/transform.z);
    }

    public FloatTransform divide(double value){
        return divide(value,value,value);
    }

    public FloatTransform divide(double valueHorizontal, double valueY){
        return divide(valueHorizontal,valueY,valueHorizontal);
    }

    public FloatTransform divide(double valueX, double valueY, double valueZ){
        return new FloatTransform((float) (this.x/valueX), (float) (this.y/valueY), (float) (this.z/valueZ));
    }

    public FloatTransform scale(double factor){
        return scale(factor,factor,factor);
    }

    public FloatTransform scale(double factorX, double factorY, double factorZ){
        return new FloatTransform((float) (this.x*factorX), (float) (this.y*factorY), (float) (this.z*factorZ));
    }

    /**
     * Normalizes the transform to a length of 1 (except if it is the zero transform)
     */
    public FloatTransform normalize(){
        double value = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return value < 1.0E-4D ? ZERO : new FloatTransform((float) (this.x / value), (float) (this.y / value), (float) (this.z / value));
    }

    /**
     * Returns a transform representing the distance between this and another transform
     * as a length of 1 (except if the result is the zero transform)
     */
    public FloatTransform difference(FloatTransform other) {
        if(this.equals(other) || (this.equals(FloatTransform.ZERO) ^ other.equals(FloatTransform.ZERO))) return FloatTransform.ZERO;
        return this.subtract(other).normalize();
    }

    public FloatTransform reverse(){
        return this.scale(-1);
    }

    public double distanceTo(FloatTransform other){
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

    public double distanceToSqr(FloatTransform other){
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

    public FloatTransform lerp(FloatTransform other, double delta){
        return new FloatTransform((float) Mth.lerp(delta,this.x,other.x), (float) Mth.lerp(delta,this.y,other.y), (float) Mth.lerp(delta,this.z,other.z));
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
    public int compareTo(@NotNull FloatTransform o) {
        int cmp = Double.compare(this.x, o.x);
        if (cmp != 0) return cmp;

        cmp = Double.compare(this.y, o.y);
        if (cmp != 0) return cmp;

        return Double.compare(this.z, o.z);
    }

    public FloatTransform xRot(float pPitch) {
        float f = Mth.cos(pPitch);
        float f1 = Mth.sin(pPitch);
        double d1 = this.y * (double)f + this.z * (double)f1;
        double d2 = this.z * (double)f - this.y * (double)f1;
        return new FloatTransform(this.x, (float) d1, (float) d2);
    }

    public FloatTransform yRot(float pYaw) {
        float f = Mth.cos(pYaw);
        float f1 = Mth.sin(pYaw);
        double d0 = this.x * (double)f + this.z * (double)f1;
        double d2 = this.z * (double)f - this.x * (double)f1;
        return new FloatTransform((float) d0, this.y, (float) d2);
    }

    public FloatTransform zRot(float pRoll) {
        float f = Mth.cos(pRoll);
        float f1 = Mth.sin(pRoll);
        double d0 = this.x * (double)f + this.y * (double)f1;
        double d1 = this.y * (double)f - this.x * (double)f1;
        return new FloatTransform((float) d0, (float) d1, this.z);
    }

    /**
     * Returns a {@link FloatTransform} from the given pitch and yaw degrees as {@link FloatTransform}.
     */
    public static FloatTransform directionFromRotation(Vec2 pVec) {
        return directionFromRotation(pVec.x, pVec.y);
    }

    /**
     * Returns a {@link FloatTransform} from the given pitch and yaw degrees.
     */
    public static FloatTransform directionFromRotation(float pPitch, float pYaw) {
        float f = Mth.cos(-pYaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float f1 = Mth.sin(-pYaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float f2 = -Mth.cos(-pPitch * ((float)Math.PI / 180F));
        float f3 = Mth.sin(-pPitch * ((float)Math.PI / 180F));
        return new FloatTransform((float) (f1 * f2), (float) f3, (float) (f * f2));
    }

    public FloatTransform align(EnumSet<Direction.Axis> pAxes) {
        double d0 = pAxes.contains(Direction.Axis.X) ? (double)Mth.floor(this.x) : this.x;
        double d1 = pAxes.contains(Direction.Axis.Y) ? (double)Mth.floor(this.y) : this.y;
        double d2 = pAxes.contains(Direction.Axis.Z) ? (double)Mth.floor(this.z) : this.z;
        return new FloatTransform((float) d0, (float) d1, (float) d2);
    }

    public double get(Direction.Axis pAxis) {
        return pAxis.choose(this.x, this.y, this.z);
    }

    public FloatTransform with(Direction.Axis pAxis, double pLength) {
        double d0 = pAxis == Direction.Axis.X ? pLength : this.x;
        double d1 = pAxis == Direction.Axis.Y ? pLength : this.y;
        double d2 = pAxis == Direction.Axis.Z ? pLength : this.z;
        return new FloatTransform((float) d0, (float) d1, (float) d2);
    }

    public FloatTransform relative(Direction pDirection, double pLength) {
        Vec3i vec3i = pDirection.getNormal();
        return new FloatTransform((float) (this.x + pLength * (float)vec3i.getX()), (float) (this.y + pLength * (float)vec3i.getY()), (float) (this.z + pLength * (float)vec3i.getZ()));
    }

    public FloatTransform offsetRandom(RandomSource pRandom, float pFactor) {
        return this.add(new FloatTransform((pRandom.nextFloat() - 0.5F) * pFactor, (pRandom.nextFloat() - 0.5F) * pFactor, (pRandom.nextFloat() - 0.5F) * pFactor));
    }

    public boolean closerThan(Position pPos, double pDistance) {
        return this.distanceToSqr(pPos.x(), pPos.y(), pPos.z()) < pDistance * pDistance;
    }

    /**
     * Returns a new transform with the result of this transform x the specified transform.
     */
    public FloatTransform cross(FloatTransform transform) {
        return new FloatTransform(this.y * transform.z - this.z * transform.y, this.z * transform.x - this.x * transform.z, this.x * transform.y - this.y * transform.x);
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

    public FloatTransform toDouble(){
        return new FloatTransform(this.x,this.y,this.z);
    }

    public IntTransform toInt(){
        return new IntTransform((int) this.x, (int) this.y, (int) this.z);
    }
}
