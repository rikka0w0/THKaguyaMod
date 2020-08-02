package thKaguyaMod;

import java.util.Random;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class THMathUtil {
	
    // This is absolutely unnecessary
	@Deprecated
    public static Vec3d angle(float yaw, float pitch) {
    	return getVecFromAngle(yaw, pitch);
    }
    
    /**
     * y軸方向にのみ働く重力のベクトルを返す
     * @gravityY y軸方向の重力。マイナスで上、プラスで下に向かう
     * @return
     */
    public static Vec3d gravity(double gravityY) {
    	return new Vec3d(0.0D, gravityY, 0.0D);
    } // TODO: gravity_Zero() -> Vec3d.ZERO
	

	/**
	 * ランダムな回転軸ベクトルを返す
	 * @return
	 */
	public static Vec3d rotate_Random() {
		Random rand = new Random();
		return angle(rand.nextFloat() * 360F, rand.nextFloat() * 180F - 90F);
	}
	
	/**
	 * 基準の方向ベクトルから指定の角度内のランダムな方向ベクトルを返す
	 * @param direction 基準となる方向ベクトル
	 * @param limitAngle ランダムに変化する限界の角度（度数）
	 * @return
	 */
	public static Vec3d angle_LimitRandom(Vec3d direction, float limitAngle) {
		Vec3d rotate = rotate_Random();
		Random rand = new Random();
		return getVectorFromRotation(rotate, direction, rand.nextFloat() * limitAngle - (limitAngle / 2.0F));

	}
    
	/**
	 * 角度を-180～180度に変換する
	 * @param angle 変換する角度
	 * @return -180～180度に変換した角度
	 */
	public static final float getAngleMax180(float angle) {
		angle %= 360F;
		if (angle > 180F) {
			angle -= 360F;
		} else if (angle < -180) {
			angle += 360F;
		}
		return angle;
	}

	/**
	 * ２つのベクトル、ベクトルA、ベクトルBの間の角度を返す（-180～180度）
	 * @param vectorA
	 * @param vectorB
	 * @return ２つのベクトルの成す角度
	 */
	public static final float getVectorAndVectorAngle(Vec3d vectorA, Vec3d vectorB) {
		double inner = vectorA.dotProduct(vectorB);
		return getAngleMax180((float)(Math.acos(inner / (vectorA.length() * vectorB.length())) / Math.PI * 180.0D));
	}

    /////////////////////////////
    /// Math Utilities
    /////////////////////////////
	
    /**
     * 生物の位置ベクトルを返す。
     * この座標のY座標はその生物の目より少し下になる
     * @return the position of the LivingEntity, with y slightly below the eye of the creature.
     */
    public static Vec3d pos_Living(LivingEntity living) {
    	return new Vec3d(living.posX, getPosYFromEye(living), living.posZ);
    }
	
	/**
	 * 目の高さも含めたY座標を返す
	 * @param living      : 目の高さを含めたY座標を取得するEntityLivingBase
	 * @param yAdjustment : 目の高さを基準に、高さの調整をさせる
	 * @return：目の高さに調整したY座標
	 */
	public static final double getPosYFromEye(LivingEntity living, double yAdjustment){
		return living.posY + living.getEyeHeight() + Math.sin((double)living.rotationPitch / 180.0D * Math.PI) * yAdjustment + yAdjustment;
	}
	
	/**
	 * 目の高さも含めたY座標を返す。目より少し下から出す標準的な設定
	 * @param living      : 目の高さを含めたY座標を取得するEntityLivingBase
	 * @return ：目の高さに調整したY座標
	 */
	public static final double getPosYFromEye(LivingEntity living) {
		return living.posY + living.getEyeHeight() + Math.sin((double)living.rotationPitch / 180.0D * Math.PI) * -0.5D + Math.cos((double)living.rotationPitch / 180.0D * Math.PI) * -0.5D;
	}
	
	/**
	 * 角度（度数）と強さからベクトルを取得する
	 * @param yaw   : 水平方向の角度（度数）
	 * @param pitch : 垂直方向の角度（度数）
	 * @param scale : ベクトルの強さ
	 * @return 角度と強さから求まった３次元ベクトル
	*/
	public static final Vec3d getVecFromAngle(float yaw, float pitch, double scale) {
		double yaw_rad = (double)yaw / 180.0D * Math.PI;
		double pitch_rad = (double)pitch / 180.0D * Math.PI;
		double vectorX = -Math.sin(yaw_rad) * Math.cos(pitch_rad) * scale;//X方向　水平方向
		double vectorY = -Math.sin(pitch_rad) * scale;//Y方向　上下
		double vectorZ =  Math.cos(yaw_rad) * Math.cos(pitch_rad) * scale;//Z方向　水平方向
		return new Vec3d(vectorX, vectorY, vectorZ);
	}

	/**
	 * 角度（度数）から単位ベクトルを取得する
	 * @param yaw   : 水平方向の角度（度数）
	 * @param pitch : 垂直方向の角度（度数）
	 * @return ：角度から求まった３次元の単位ベクトル
	*/
	public static final Vec3d getVecFromAngle(float yaw, float pitch) {
		return getVecFromAngle(yaw, pitch, 1.0D);
	}
	
	/**
	 * 任意の回転軸rotateVectorに対して、任意のベクトルangleVectorがangle度回転したベクトルを返す
	 * @param rotateVectorX 回転の軸にしたいベクトルのX成分
	 * @param rotateVectorY
	 * @param rotateVectorZ
	 * @param angleVectorX 回転したいベクトルのX成分
	 * @param angleVectorY
	 * @param angleVectorZ
	 * @param angle 回転させたい角度（度数）
	 * @return 任意の回転軸rotateVectorに対して、任意のベクトルangleVectorがangle度回転したベクトル。
	 */
	public static Vec3d getVectorFromRotation(double rotateVectorX, double rotateVectorY, double rotateVectorZ, 
											double angleVectorX, double angleVectorY, double angleVectorZ, float angle) {
		double angleRad = (double)angle / 180.0D * (double)Math.PI;
		double sinA = Math.sin(angleRad);
		double cosA = Math.cos(angleRad);
		double returnVectorX = (rotateVectorX * rotateVectorX * (1 - cosA) + cosA)              * angleVectorX + (rotateVectorX * rotateVectorY * (1 - cosA) - rotateVectorZ * sinA) * angleVectorY + (rotateVectorZ * rotateVectorX * (1 - cosA) + rotateVectorY * sinA) * angleVectorZ;
		double returnVectorY = (rotateVectorX * rotateVectorY * (1 - cosA) + rotateVectorZ * sinA) * angleVectorX + (rotateVectorY * rotateVectorY * (1 - cosA) + cosA)              * angleVectorY + (rotateVectorY * rotateVectorZ * (1 - cosA) - rotateVectorX * sinA) * angleVectorZ;
		double returnVectorZ = (rotateVectorZ * rotateVectorX * (1 - cosA) - rotateVectorY * sinA) * angleVectorX + (rotateVectorY * rotateVectorZ * (1 - cosA) + rotateVectorX * sinA) * angleVectorY + (rotateVectorZ * rotateVectorZ * (1 - cosA) + cosA)              * angleVectorZ;
		
		return new Vec3d(returnVectorX, returnVectorY, returnVectorZ);
	}
	
	/**
	 * 任意の回転軸rotateVectorに対して、任意のベクトルangleVectorがangle度回転したベクトルを返す
	 * @param rotateVector 回転の軸にしたいベクトル
	 * @param angleVector 回転したい方向ベクトル
	 * @param angle 回転させたい角度（度数）
	 * @return 任意の回転軸rotateVectorに対して、任意のベクトルangleVectorがangle度回転したベクトル。
	 */
	public static Vec3d getVectorFromRotation(Vec3d rotate, Vec3d angleVector, float angle) {
		return getVectorFromRotation(
				rotate.x, rotate.y, rotate.z, 
				angleVector.x, angleVector.y, angleVector.z, 
				angle);
	}
}
