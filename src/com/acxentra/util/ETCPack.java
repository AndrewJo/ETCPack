package com.acxentra.util;

import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

/**
 * Written by Andrew Jo.
 * @author Andrew Jo
 * This code is licensed under Apache License, version 2.0
 * See LICENSE.md for more information.
 */
public final class ETCPack {
	public static final String LOG = "ETCPack";
	public enum CompressionSpeed {
		FAST,
		MEDIUM,
		SLOW
	}
	
	public enum ErrorMetric {
		PERCEPTUAL,
		NON_PERCEPTUAL
	}
	
	// The error metric Wr Wg Wb should be definied so that Wr^2 + Wg^2 + Wb^2 = 1.
	// Hence it is easier to first define the squared values and derive the weights
	// as their square-roots.
	
	public static final float PERCEPTUAL_WEIGHT_R_SQUARED = 0.299f;
	public static final float PERCEPTUAL_WEIGHT_G_SQUARED = 0.587f;
	public static final float PERCEPTUAL_WEIGHT_B_SQUARED = 0.114f;
	
	// Alternative weights
	//public static final float PERCEPTUAL_WEIGHT_R_SQUARED = 0.3086f;
	//public static final float PERCEPTUAL_WEIGHT_G_SQUARED = 0.6094f;
	//public static final float PERCEPTUAL_WEIGHT_B_SQUARED = 0.082f;
	
	public static final float PERCEPTUAL_WEIGHT_R = (float)Math.sqrt(PERCEPTUAL_WEIGHT_R_SQUARED);
	public static final float PERCEPTUAL_WEIGHT_G = (float)Math.sqrt(PERCEPTUAL_WEIGHT_G_SQUARED);
	public static final float PERCEPTUAL_WEIGHT_B = (float)Math.sqrt(PERCEPTUAL_WEIGHT_B_SQUARED);
	
	double wR = PERCEPTUAL_WEIGHT_R;
	double wG = PERCEPTUAL_WEIGHT_G;
	double wB = PERCEPTUAL_WEIGHT_B;
	
	double wR2 = PERCEPTUAL_WEIGHT_R_SQUARED;
	double wG2 = PERCEPTUAL_WEIGHT_G_SQUARED;
	double wB2 = PERCEPTUAL_WEIGHT_B_SQUARED;
	
	// SLOW SCAN RANGE IS -5 to 5 in all three colors
	public static final int SLOW_SCAN_MIN = -5;
	public static final int SLOW_SCAN_MAX = 5;
	public static final int SLOW_SCAN_RANGE = SLOW_SCAN_MAX - SLOW_SCAN_MIN + 1;
	public static final int SLOW_SCAN_OFFSET = -SLOW_SCAN_MIN;
	// We need this to guarrantee that at least one try is valid
	public static final int SLOW_TRY_MIN = -4 - SLOW_SCAN_MAX;
	public static final int SLOW_TRY_MAX = 3 - SLOW_SCAN_MIN;
	
	
	// MEDIUM SCAN RANGE IS -3 to 3in all three colors
	public static final int MEDIUM_SCAN_MIN = -3;
	public static final int MEDIUM_SCAN_MAX = 3;
	public static final int MEDIUM_SCAN_RANGE = MEDIUM_SCAN_MAX - MEDIUM_SCAN_MIN + 1;
	public static final int MEDIUM_SCAN_OFFSET = -MEDIUM_SCAN_MIN;

	// We need this to guarrantee that at least one try is valid
	public static final int MEDIUM_TRY_MIN = -4 - MEDIUM_SCAN_MAX;
	public static final int MEDIUM_TRY_MAX = 3 - MEDIUM_SCAN_MIN;
	
	
	int[] scramble = new int[] {3, 2, 0, 1};
	
	public enum PpmFileFormat {
		FIRST_PIXEL_IN_PPM_FILE_MAPS_TO_S0T0,
		FIRST_PIXEL_IN_PPM_FILE_MAPS_TO_S0T1
	}
	
	static PpmFileFormat orientation = PpmFileFormat.FIRST_PIXEL_IN_PPM_FILE_MAPS_TO_S0T0;
	
	int ktx_mode;
	
	public final class KtxHeader {
		private int[] identifier = new int[12];
		private int endianness;
		private int glType;
		private int glTypeSize;
		private int glFormat;
		private int glInternalFormat;
		private int glBaseInternalFormat;
		private int pixelWidth;
		private int pixelHeight;
		private int pixelDepth;
		private int numberOfArrayElements;
		private int numberOfFaces;
		private int numberOfMipmapLevels;
		private int bytesOfKeyValueData;
		
		public int[] getIdentifier() { return identifier; }
		public void setIdentifier(int[] identifier) { this.identifier = identifier; }
		
		public int getEndianness() { return endianness; }
		public void setEndianness(int endianness) { this.endianness = endianness; }
		
		public int getGlType() { return glType; }
		public void setGlType(int glType) { this.glType = glType; }
		
		public int getGlTypeSize() { return glTypeSize; }
		public void setGlTypeSize(int glTypeSize) { this.glTypeSize = glTypeSize; }
		
		public int getGlFormat() { return glFormat; }
		public void setGlFormat(int glFormat) { this.glFormat = glFormat; }
		
		public int getGlInternalFormat() { return glInternalFormat; }
		public void setGlInternalFormat(int glInternalFormat) { this.glInternalFormat = glInternalFormat; }
		
		public int getGlBaseInternalFormat() { return glBaseInternalFormat; }
		public void setGlBaseInternalFormat(int glBaseInternalFormat) { this.glBaseInternalFormat = glBaseInternalFormat; }
		
		public int getPixelWidth() { return pixelWidth; }
		public void setPixelWidth(int pixelWidth) { this.pixelWidth = pixelWidth; }
		
		public int getPixelHeight() { return pixelHeight; }
		public void setPixelHeight(int pixelHeight) { this.pixelHeight = pixelHeight; }
		
		public int getPixelDepth() { return pixelDepth; }
		public void setPixelDepth(int pixelDepth) { this.pixelDepth = pixelDepth; }
		
		public int getNumberOfArrayElements() { return numberOfArrayElements; }
		public void setNumberOfArrayElements(int numberOfArrayElements) { this.numberOfArrayElements = numberOfArrayElements; }
		
		public int getNumberOfFaces() { return numberOfFaces; }
		public void setNumberOfFaces(int numberOfFaces) { this.numberOfFaces = numberOfFaces; }
		
		public int getNumberOfMipmapLevels() { return numberOfMipmapLevels; }
		public void setNumberOfMipmapLevels(int numberOfMipmapLevels) { this.numberOfMipmapLevels = numberOfMipmapLevels; }
		
		public int getBytesOfKeyValueData() { return bytesOfKeyValueData; }
		public void setBytesOfKeyValueData(int bytesOfKeyValueData) { this.bytesOfKeyValueData = bytesOfKeyValueData; }
	}
	
	public static final int[] KTX_IDENTIFIER_REF = new int[] { 0xAB, 0x4B, 0x54, 0x58, 0x20, 0x31, 0x31, 0xBB, 0x0D, 0x0A, 0x1A, 0x0A }; 
	public static final int KTX_ENDIAN_REF = 0x04030201;
	public static final int KTX_ENDIAN_REF_REV = 0x01020304;
	
	int[] ktx_identifier = KTX_IDENTIFIER_REF;
	
	static int[][] compressParamsEnc = new int[16][4];
	
	static boolean readSrcFile() {
		int w1, h1;
		int wdiv4, hdiv4;
		
		boolean flip;
		
		flip = orientation == PpmFileFormat.FIRST_PIXEL_IN_PPM_FILE_MAPS_TO_S0T0;
		
		// TODO: Remove this later.
		return false;
	}
	
	public static void readCompressParamsEnc() {
		compressParamsEnc[0][0]  =  -8; compressParamsEnc[0][1]  =  -2; compressParamsEnc[0][2]  =  2; compressParamsEnc[0][3]  =   8;
		compressParamsEnc[1][0]  =  -8; compressParamsEnc[1][1]  =  -2; compressParamsEnc[1][2]  =  2; compressParamsEnc[1][3]  =   8;
		compressParamsEnc[2][0]  = -17; compressParamsEnc[2][1]  =  -5; compressParamsEnc[2][2]  =  5; compressParamsEnc[2][3]  =  17;
		compressParamsEnc[3][0]  = -17; compressParamsEnc[3][1]  =  -5; compressParamsEnc[3][2]  =  5; compressParamsEnc[3][3]  =  17;
		compressParamsEnc[4][0]  = -29; compressParamsEnc[4][1]  =  -9; compressParamsEnc[4][2]  =  9; compressParamsEnc[4][3]  =  29;
		compressParamsEnc[5][0]  = -29; compressParamsEnc[5][1]  =  -9; compressParamsEnc[5][2]  =  9; compressParamsEnc[5][3]  =  29;
		compressParamsEnc[6][0]  = -42; compressParamsEnc[6][1]  = -13; compressParamsEnc[6][2]  = 13; compressParamsEnc[6][3]  =  42;
		compressParamsEnc[7][0]  = -42; compressParamsEnc[7][1]  = -13; compressParamsEnc[7][2]  = 13; compressParamsEnc[7][3]  =  42;
		compressParamsEnc[8][0]  = -60; compressParamsEnc[8][1]  = -18; compressParamsEnc[8][2]  = 18; compressParamsEnc[8][3]  =  60;
		compressParamsEnc[9][0]  = -60; compressParamsEnc[9][1]  = -18; compressParamsEnc[9][2]  = 18; compressParamsEnc[9][3]  =  60;
		compressParamsEnc[10][0] = -80; compressParamsEnc[10][1] = -24; compressParamsEnc[10][2] = 24; compressParamsEnc[10][3] =  80;
		compressParamsEnc[11][0] = -80; compressParamsEnc[11][1] = -24; compressParamsEnc[11][2] = 24; compressParamsEnc[11][3] =  80;
		compressParamsEnc[12][0] =-106; compressParamsEnc[12][1] = -33; compressParamsEnc[12][2] = 33; compressParamsEnc[12][3] = 106;
		compressParamsEnc[13][0] =-106; compressParamsEnc[13][1] = -33; compressParamsEnc[13][2] = 33; compressParamsEnc[13][3] = 106;
		compressParamsEnc[14][0] =-183; compressParamsEnc[14][1] = -47; compressParamsEnc[14][2] = 47; compressParamsEnc[14][3] = 183;
		compressParamsEnc[15][0] =-183; compressParamsEnc[15][1] = -47; compressParamsEnc[15][2] = 47; compressParamsEnc[15][3] = 183;
	}
	
	public static void compressFile(InputStream srcFileStream, OutputStream dstFileStream, CompressionSpeed compressionSpeed, ErrorMetric errorMetric) {
		Log.i(LOG, "Using " + compressionSpeed.toString() + " compression mode and " + errorMetric.toString() + " error metric");
		Log.v(LOG, orientation == PpmFileFormat.FIRST_PIXEL_IN_PPM_FILE_MAPS_TO_S0T0 ? "s=0, t=0." : "s=0, t=1.");
		
		readCompressParamsEnc();
	}
}

