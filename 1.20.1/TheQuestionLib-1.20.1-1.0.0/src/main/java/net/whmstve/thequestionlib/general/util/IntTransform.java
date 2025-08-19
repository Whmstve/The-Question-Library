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

public class IntTransform implements Position, Comparable<IntTransform> {
    private final int x;
    private final int y;
    private final int z;

    public static final IntTransform ZERO = new IntTransform(0,0,0);

    public IntTransform(int x, int y, int z){
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

    public static IntTransform fromPosition(@NotNull Vec3 position){
        return new IntTransform((int) position.x(), (int) position.y(), (int) position.z());
    }

    public static IntTransform fromPosition(@NotNull Vec3i position){
        return new IntTransform(position.getX(), position.getY(), position.getZ());
    }

    public static IntTransform combine(@NotNull Position a, @NotNull Position b){
        return new IntTransform((int) (a.x()+b.x()), (int) (a.y()+b.y()), (int) (a.z()+b.z()));
    }

    public static IntTransform split(@NotNull Position a, @NotNull Position b){
        return new IntTransform((int) (a.x()-b.x()), (int) (a.y()-b.y()), (int) (a.z()-b.z()));
    }

    public static IntTransform combine(@NotNull Vec3i a, @NotNull Vec3i b){
        return new IntTransform(a.getX()+b.getX(),a.getY()+b.getY(),a.getZ()+b.getZ());
    }

    public static IntTransform split(@NotNull Vec3i a, @NotNull Vec3i b){
        return new IntTransform(a.getX()-b.getX(),a.getY()-b.getY(),a.getZ()-b.getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntTransform transform = (IntTransform) o;
        return Double.compare(transform.x, x) == 0 && Double.compare(transform.y, y) == 0 && Double.compare(transform.z, z) == 0;
    }

    public static IntTransform max(IntTransform a, IntTransform b){
        if(a.equals(b)) return a;
        if(a.compareTo(b)<0) return b;
        else return a;
    }

    public static IntTransform min(IntTransform a, IntTransform b){
        if(a.equals(b)) return a;
        if(a.compareTo(b)>0) return b;
        else return a;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public List<IntTransform> draw(IntTransform b) {
        List<IntTransform> result = new ArrayList<>();
        IntTransform direction = this.difference(b);
        double length = Math.sqrt(this.distanceToSqr(b));
        IntTransform unitStep = direction.normalize();

        IntTransform current = this;
        for (double dist = 0; dist <= length; dist ++) {
            result.add(current);
            current = current.add(unitStep);
        }

        return result;
    }


    public IntTransform add(IntTransform transform){
        return new IntTransform(this.x+transform.x,this.y+transform.y, this.z+transform.z);
    }

    public IntTransform add(int value){
        return add(value,value,value);
    }

    public IntTransform add(int valueHorizontal, int valueY){
        return add(valueHorizontal,valueY,valueHorizontal);
    }

    public IntTransform add(int valueX, int valueY, int valueZ){
        return new IntTransform((int) (this.x+valueX), (int) (this.y+valueY), (int) (this.z+valueZ));
    }

    public IntTransform subtract(IntTransform transform){
        return new IntTransform(this.x-transform.x,this.y-transform.y, this.z-transform.z);
    }

    public IntTransform subtract(int value){
        return subtract(value,value,value);
    }

    public IntTransform subtract(int valueHorizontal, int valueY){
        return subtract(valueHorizontal,valueY,valueHorizontal);
    }

    public IntTransform subtract(int valueX, int valueY, int valueZ){
        return new IntTransform((int) (this.x-valueX), (int) (this.y-valueY), (int) (this.z-valueZ));
    }

    public IntTransform multiply(IntTransform transform){
        return new IntTransform(this.x*transform.x,this.y*transform.y, this.z*transform.z);
    }

    public IntTransform multiply(int value){
        return multiply(value,value,value);
    }

    public IntTransform multiply(int valueHorizontal, int valueY){
        return multiply(valueHorizontal,valueY,valueHorizontal);
    }

    public IntTransform multiply(int valueX, int valueY, int valueZ){
        return new IntTransform(this.x*valueX, this.y*valueY, this.z*valueZ);
    }

    public IntTransform multiply(Matrix3f mat){
        return new IntTransform(
                (int) Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                (int) Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                (int) Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z))
        );
    }

    public IntTransform multiply(Matrix3fc mat){
        return new IntTransform(
                (int) Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                (int) Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                (int) Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z))
        );
    }

    public IntTransform multiply(Matrix3dc mat){
        return new IntTransform(
                (int) Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                (int) Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                (int) Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z))
        );
    }

    public IntTransform multiply(Matrix3x2fc mat){
        return new IntTransform(
                (int) Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)),
                (int) Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)),
                z
        );
    }

    public IntTransform divide(IntTransform transform){
        return new IntTransform((int) (this.x/transform.x()), (int) (this.y/transform.y()), (int) (this.z/transform.z()));
    }

    public IntTransform divide(int value){
        return divide(value,value,value);
    }

    public IntTransform divide(int valueHorizontal, int valueY){
        return divide(valueHorizontal,valueY,valueHorizontal);
    }

    public IntTransform divide(int valueX, int valueY, int valueZ){
        return new IntTransform((int) (this.x/valueX), (int) (this.y/valueY), (int) (this.z/valueZ));
    }

    public IntTransform scale(int factor){
        return scale(factor,factor,factor);
    }

    public IntTransform scale(int factorX, int factorY, int factorZ){
        return new IntTransform((int) (this.x*factorX), (int) (this.y*factorY), (int) (this.z*factorZ));
    }

    /**
     * Normalizes the transform to a length of 1 (except if it is the zero transform)
     */
    public IntTransform normalize(){
        double value = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return value < 1.0E-4D ? ZERO : new IntTransform((int) (this.x / value), (int) (this.y / value), (int) (this.z / value));
    }

    /**
     * Returns a transform representing the distance between this and another transform
     * as a length of 1 (except if the result is the zero transform)
     */
    public IntTransform difference(IntTransform other) {
        if(this.equals(other) || (this.equals(IntTransform.ZERO) ^ other.equals(IntTransform.ZERO))) return IntTransform.ZERO;
        return this.subtract(other).normalize();
    }

    public IntTransform reverse(){
        return this.scale(-1);
    }

    public int distanceTo(IntTransform other){
        int dx = other.x - this.x;
        int dy = other.y - this.y;
        int dz = other.z - this.z;
        return (int) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public int distanceTo(int nx, int ny, int nz){
        int dx = nx - this.x;
        int dy = ny - this.y;
        int dz = nz - this.z;
        return (int) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public int distanceToSqr(IntTransform other){
        int dx = other.x - this.x;
        int dy = other.y - this.y;
        int dz = other.z - this.z;
        return (dx * dx + dy * dy + dz * dz);
    }

    public int distanceToSqr(int nx, int ny, int nz){
        int dx = nx - this.x;
        int dy = ny - this.y;
        int dz = nz - this.z;
        return (dx * dx + dy * dy + dz * dz);
    }

    public IntTransform lerp(IntTransform other, int delta){
        return new IntTransform((int) Mth.lerp(delta,this.x,other.x), (int) Mth.lerp(delta,this.y,other.y), (int) Mth.lerp(delta,this.z,other.z));
    }

    /**
     * Returns the length of the transform.
     */
    public int length() {
        return (int) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public int lengthSqr() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public int horizontalDistance() {
        return (int) Math.sqrt(this.x * this.x + this.z * this.z);
    }

    public int horizontalDistanceSqr() {
        return this.x * this.x + this.z * this.z;
    }

    @Override
    public int compareTo(@NotNull IntTransform o) {
        int cmp = Double.compare(this.x, o.x);
        if (cmp != 0) return cmp;

        cmp = Double.compare(this.y, o.y);
        if (cmp != 0) return cmp;

        return Double.compare(this.z, o.z);
    }

    public IntTransform xRot(float pPitch) {
        float f = Mth.cos(pPitch);
        float f1 = Mth.sin(pPitch);
        int d1 = this.y * (int)f + this.z * (int)f1;
        int d2 = this.z * (int)f - this.y * (int)f1;
        return new IntTransform(this.x, (int) d1, (int) d2);
    }

    public IntTransform yRot(float pYaw) {
        float f = Mth.cos(pYaw);
        float f1 = Mth.sin(pYaw);
        int d0 = this.x * (int)f + this.z * (int)f1;
        int d2 = this.z * (int)f - this.x * (int)f1;
        return new IntTransform((int) d0, this.y, (int) d2);
    }

    public IntTransform zRot(float pRoll) {
        float f = Mth.cos(pRoll);
        float f1 = Mth.sin(pRoll);
        int d0 = this.x * (int)f + this.y * (int)f1;
        int d1 = this.y * (int)f - this.x * (int)f1;
        return new IntTransform(d0, d1, this.z);
    }

    /**
     * Returns a {@link IntTransform} from the given pitch and yaw degrees as {@link IntTransform}.
     */
    public static IntTransform directionFromRotation(Vec2 pVec) {
        return directionFromRotation(pVec.x, pVec.y);
    }

    /**
     * Returns a {@link IntTransform} from the given pitch and yaw degrees.
     */
    public static IntTransform directionFromRotation(float pPitch, float pYaw) {
        float f = Mth.cos(-pYaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float f1 = Mth.sin(-pYaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float f2 = -Mth.cos(-pPitch * ((float)Math.PI / 180F));
        float f3 = Mth.sin(-pPitch * ((float)Math.PI / 180F));
        return new IntTransform((int) (f1 * f2), (int) f3, (int) (f * f2));
    }

    public IntTransform align(EnumSet<Direction.Axis> pAxes) {
        int d0 = pAxes.contains(Direction.Axis.X) ? (int)Mth.floor(this.x) : this.x;
        int d1 = pAxes.contains(Direction.Axis.Y) ? (int)Mth.floor(this.y) : this.y;
        int d2 = pAxes.contains(Direction.Axis.Z) ? (int)Mth.floor(this.z) : this.z;
        return new IntTransform((int) d0, (int) d1, (int) d2);
    }

    public int get(Direction.Axis pAxis) {
        return pAxis.choose(this.x, this.y, this.z);
    }

    public IntTransform with(Direction.Axis pAxis, int pLength) {
        int d0 = pAxis == Direction.Axis.X ? pLength : this.x;
        int d1 = pAxis == Direction.Axis.Y ? pLength : this.y;
        int d2 = pAxis == Direction.Axis.Z ? pLength : this.z;
        return new IntTransform((int) d0, (int) d1, (int) d2);
    }

    public IntTransform relative(Direction pDirection, int pLength) {
        Vec3i vec3i = pDirection.getNormal();
        return new IntTransform((int) (this.x + pLength * (float)vec3i.getX()), (int) (this.y + pLength * (float)vec3i.getY()), (int) (this.z + pLength * (float)vec3i.getZ()));
    }

    public IntTransform offsetRandom(RandomSource pRandom, float pFactor) {
        return this.add(new IntTransform((int) ((pRandom.nextFloat() - 0.5F) * pFactor), (int) ((pRandom.nextFloat() - 0.5F) * pFactor), (int) ((pRandom.nextFloat() - 0.5F) * pFactor)));
    }

    public boolean closerThan(Position pPos, double pDistance) {
        return this.distanceToSqr((int) pPos.x(), (int) pPos.y(), (int) pPos.z()) < pDistance * pDistance;
    }

    /**
     * Returns a new transform with the result of this transform x the specified transform.
     */
    public IntTransform cross(IntTransform transform) {
        return new IntTransform(this.y * transform.z - this.z * transform.y, this.z * transform.x - this.x * transform.z, this.x * transform.y - this.y * transform.x);
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

    public Transform toDouble(){
        return new Transform(this.x,this.y,this.z);
    }

    public IntTransform toFloat(){
        return new IntTransform((int) this.x, (int) this.y, (int) this.z);
    }
}
