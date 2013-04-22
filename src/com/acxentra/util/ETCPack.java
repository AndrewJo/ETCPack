package com.acxentra.util;

public final class ETCPack {
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
	
	int orientation;
	
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
}

