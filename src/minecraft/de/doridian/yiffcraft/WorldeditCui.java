package de.doridian.yiffcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldeditCui {

    public static int testBlockX[] = new int[2];
    public static int testBlockY[] = new int[2];
    public static int testBlockZ[] = new int[2];
    public static boolean testBlockActive[] = new boolean[2];
	public static int numHighlights=0;
    public static int maxBlockX;
    public static int maxBlockY;
    public static int maxBlockZ;
    public static int minBlockX;
    public static int minBlockY;
    public static int minBlockZ;
    public static double curRenderTick;

    private static void drawBoundingBox(float red, float green, float blue, float alpha, double minX, double minY,
                                 double minZ, double maxX, double maxY, double maxZ) {
        Tessellator tessellator = Tessellator.instance;
        
        // Draw bottom face using LineStrip
        tessellator.startDrawing(3);
        tessellator.setColorRGBA_F(red, green, blue, alpha);
        tessellator.addVertex(minX, minY, minZ);
        tessellator.addVertex(maxX, minY, minZ);
        tessellator.addVertex(maxX, minY, maxZ);
        tessellator.addVertex(minX, minY, maxZ);
        tessellator.addVertex(minX, minY, minZ);
        tessellator.draw();

        // Draw top face using LineStrip
        tessellator.startDrawing(3);
        tessellator.setColorRGBA_F(red, green, blue, alpha);
        tessellator.addVertex(minX, maxY, minZ); 
        tessellator.addVertex(maxX, maxY, minZ);
        tessellator.addVertex(maxX, maxY, maxZ);
        tessellator.addVertex(minX, maxY, maxZ);
        tessellator.addVertex(minX, maxY, minZ);
        tessellator.draw();

        // Draw join top and bottom faces using LineList
        tessellator.startDrawing(1);
        tessellator.setColorRGBA_F(red, green, blue, alpha);
        
        tessellator.addVertex(minX, minY, minZ);
        tessellator.addVertex(minX, maxY, minZ);
        
        tessellator.addVertex(maxX, minY, minZ);
        tessellator.addVertex(maxX, maxY, minZ);
        
        tessellator.addVertex(maxX, minY, maxZ);
        tessellator.addVertex(maxX, maxY, maxZ);
        
        tessellator.addVertex(minX, minY, maxZ);
        tessellator.addVertex(minX, maxY, maxZ);
        
        tessellator.draw();
    }
    
    private static void drawLatticeBox(float red, float green, float blue, float alpha, double minX, double minY,
                                 double minZ, double maxX, double maxY, double maxZ)
    {
        Tessellator tessellator = Tessellator.instance;
    	tessellator.startDrawing(1);
        tessellator.setColorRGBA_F(red, green, blue, alpha);
        //XY plane
        double offsetsize = 1f;
        for(double xoffset = 0; ((double)xoffset+minX)<=maxX; xoffset+=offsetsize)
        {
        	for(double yoffset=0; ((double)yoffset+minY)<=maxY; yoffset+=offsetsize)
        	{
        		tessellator.addVertex(((double)xoffset+minX), ((double)yoffset+minY), minZ);
        		tessellator.addVertex(((double)xoffset+minX), ((double)yoffset+minY), maxZ);
        	}
        }
        //XZ plane
        for(double xoffset = 0; ((double)xoffset+minX)<=maxX; xoffset+=offsetsize)
        {
        	for(double zoffset=0; ((double)zoffset+minZ)<=maxZ; zoffset+=offsetsize)
        	{
        		tessellator.addVertex(((double)xoffset+minX), minY, ((double)zoffset+minZ));
        		tessellator.addVertex(((double)xoffset+minX), maxY, ((double)zoffset+minZ));
        	}
        }
        //YZ
        for(double yoffset = 0; ((double)yoffset+minY)<=maxY; yoffset+=offsetsize)
        {
        	for(double zoffset=0; ((double)zoffset+minZ)<=maxZ; zoffset+=offsetsize)
        	{
        		tessellator.addVertex(minX, ((double)yoffset+minY), ((double)zoffset+minZ));
        		tessellator.addVertex(maxX, ((double)yoffset+minY), ((double)zoffset+minZ));
        	}
        }
        tessellator.draw();
    }
    private static void drawGridSurface(float red, float green, float blue, float alpha, double minX, double minY,
                                 double minZ, double maxX, double maxY, double maxZ)
    {
        Tessellator tessellator = Tessellator.instance;
    	tessellator.startDrawing(1);
        tessellator.setColorRGBA_F(red, green, blue, alpha);
        double x,y,z;
        double offsetSize = 1.0;
        
        //Zmax XY plane, y axis
        z=maxZ;
        y=minY;
        int msize=150;
        if((maxY-y/offsetSize) < msize)
        for(double yoff=0;yoff+y<=maxY;yoff+=offsetSize)
        {
        	tessellator.addVertex(minX, y+yoff, z);
        	tessellator.addVertex(maxX, y+yoff, z);
        }
        
        //Zmin XY plane, y axis
        z=minZ;
        if((maxY-y/offsetSize) < msize)
        for(double yoff=0;yoff+y<=maxY;yoff+=offsetSize)
        {
        	tessellator.addVertex(minX, y+yoff, z);
        	tessellator.addVertex(maxX, y+yoff, z);
        }
        
        
        //Xmin YZ plane, y axis
        x=minX;
        if((maxY-y/offsetSize) < msize)
        for(double yoff=0;yoff+y<=maxY;yoff+=offsetSize)
        {
        	tessellator.addVertex(x, y+yoff, minZ);
        	tessellator.addVertex(x, y+yoff, maxZ);
        }
        
        //Xmax YZ plane, y axis
        x=maxX;
        if((maxY-y/offsetSize) < msize)
        for(double yoff=0;yoff+y<=maxY;yoff+=offsetSize)
        {
        	tessellator.addVertex(x, y+yoff, minZ);
        	tessellator.addVertex(x, y+yoff, maxZ);
        }
        
        //Zmin XY plane, x axis
        x=minX;
        z=minZ;
        if((maxX-x/offsetSize) < msize)
        for(double xoff=0;xoff+x<=maxX;xoff+=offsetSize)
        {
        	tessellator.addVertex(x+xoff, minY, z);
        	tessellator.addVertex(x+xoff, maxY, z);
        }
        //Zmax XY plane, x axis
        z=maxZ;
        if((maxX-x/offsetSize) < msize)
        for(double xoff=0;xoff+x<=maxX;xoff+=offsetSize)
        {
        	tessellator.addVertex(x+xoff, minY, z);
        	tessellator.addVertex(x+xoff, maxY, z);
        }
        //Ymin XZ plane, x axis
        y=maxY;
        if((maxX-x/offsetSize) < msize)
        for(double xoff=0;xoff+x<=maxX;xoff+=offsetSize)
        {
        	tessellator.addVertex(x+xoff, y, minZ);
        	tessellator.addVertex(x+xoff, y, maxZ);
        }
        //Ymax XZ plane, x axis
        y=minY;
        if((maxX-x/offsetSize) < msize)
        for(double xoff=0;xoff+x<=maxX;xoff+=offsetSize)
        {
        	tessellator.addVertex(x+xoff, y, minZ);
        	tessellator.addVertex(x+xoff, y, maxZ);
        }
        
        //Ymin XZ plane, z axis
        z=minZ;
        y=minY;
        if((maxZ-z/offsetSize) < msize)
        for(double zoff=0;zoff+z<=maxZ;zoff+=offsetSize)
        {
        	tessellator.addVertex(minX, y, z+zoff);
        	tessellator.addVertex(maxX, y, z+zoff);
        }
        //Ymax XZ plane, z axis
        y=maxY;
        if((maxZ-z/offsetSize) < msize)
        for(double zoff=0;zoff+z<=maxZ;zoff+=offsetSize)
        {
        	tessellator.addVertex(minX, y, z+zoff);
        	tessellator.addVertex(maxX, y, z+zoff);
        }
        //Xmin YZ plane, z axis
        x=maxX;
        if((maxZ-z/offsetSize) < msize)
        for(double zoff=0;zoff+z<=maxZ;zoff+=offsetSize)
        {
        	tessellator.addVertex(x, minY, z+zoff);
        	tessellator.addVertex(x, maxY, z+zoff);
        }
        //Xmax YZ plane, z axis
        x=minX;
        if((maxZ-z/offsetSize) < msize)
        for(double zoff=0;zoff+z<=maxZ;zoff+=offsetSize)
        {
        	tessellator.addVertex(x, minY, z+zoff);
        	tessellator.addVertex(x, maxY, z+zoff);
        }
        
        
        tessellator.draw();
    }

    private static void renderColouredBoundingBox(Minecraft mc, float red, float green, float blue, float alpha, double minX, double minY, double minZ, double maxX, double maxY,
                                           double maxZ) {
    	double playerPosX, playerPosY, playerPosZ;
		
		EntityLiving entityliving = Yiffcraft.minecraft.renderViewEntity;
		playerPosX = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)curRenderTick;
        playerPosY = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)curRenderTick;
        playerPosZ = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)curRenderTick;
		
        drawBoundingBox(red, green, blue, alpha, minX - 0.002D - playerPosX, minY - 0.002D - playerPosY, minZ - 0.002D - playerPosZ, (maxX + 0.002D) - playerPosX, (maxY + 0.002D) - playerPosY, (maxZ + 0.002D) - playerPosZ);
        
    }
    private static void renderColoredGridSurface(Minecraft mc, float red, float green, float blue, float alpha, double minX, double minY, double minZ, double maxX, double maxY,
            double maxZ)
    {
    	double playerPosX, playerPosY, playerPosZ;
		
		EntityLiving entityliving = Yiffcraft.minecraft.renderViewEntity;
		playerPosX = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)curRenderTick;
        playerPosY = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)curRenderTick;
        playerPosZ = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)curRenderTick;
		
    	drawGridSurface(red, green, blue, alpha, minX - playerPosX, minY - playerPosY, minZ - playerPosZ, maxX - playerPosX, maxY - playerPosY, maxZ - playerPosZ);
        
    }
    
	private static void renderColoredLatticeBox(Minecraft mc, float red, float green, float blue, float alpha, double minX, double minY, double minZ, double maxX, double maxY,
            double maxZ)
    {
    	double playerPosX, playerPosY, playerPosZ;
		
		EntityLiving entityliving = Yiffcraft.minecraft.renderViewEntity;
		playerPosX = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)curRenderTick;
        playerPosY = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)curRenderTick;
        playerPosZ = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)curRenderTick;
		
    	drawLatticeBox(red, green, blue, alpha, minX - playerPosX, minY - playerPosY, minZ - playerPosZ, maxX - playerPosX, maxY - playerPosY, maxZ - playerPosZ);
        
    }

    private static void renderBoundingBoxes(Minecraft mc, float[]... colors) {
        /*GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(4F);
        GL11.glDisable(3553);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);*/

        for(int index = 0; index < numHighlights; index++) {
        	if(!testBlockActive[index])
        		continue;
            int myBlockMinX = testBlockX[index];
            int myBlockMinY = testBlockY[index];
            int myBlockMinZ = testBlockZ[index];
            int myBlockMaxX = testBlockX[index] + 1;
            int myBlockMaxY = testBlockY[index] + 1;
            int myBlockMaxZ = testBlockZ[index] + 1;
            renderColouredBoundingBox(mc, colors[index][0], colors[index][1], colors[index][2], colors[index][3], myBlockMinX, myBlockMinY, myBlockMinZ, myBlockMaxX, myBlockMaxY, myBlockMaxZ);
        }

        /*GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(3008);*/
    }
    public static final float[] greenuppercolor = new float[]{0.2f,0.8f,0.2f,0.5f};
    public static final float[] blueuppercolor = new float[]{0.2f,0.2f,0.8f,0.5f};
    public static final float[] greenlowercolor = new float[]{0.2f,0.8f,0.2f,0.7f};
    public static final float[] bluelowercolor = new float[]{0.2f,0.2f,0.8f, 0.7f};

    public static void worldRender(Minecraft mc, float renderTick) {
    	curRenderTick = (double)renderTick;
        GL11.glBlendFunc(770 /*GL_SRC_ALPHA*/, 771 /*GL_ONE_MINUS_SRC_ALPHA*/);
        GL11.glLineWidth(2F);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDepthMask(false);
        //GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        GL11.glDepthFunc(GL11.GL_GEQUAL);
        renderColouredBoundingBox(mc, 0.4F, 0.1F, 0.1F, 0.2f, minBlockX, minBlockY, minBlockZ, maxBlockX, maxBlockY, maxBlockZ);
        renderColoredGridSurface(mc, 0.4F, 0.1F, 0.1F, 0.1f, minBlockX, minBlockY, minBlockZ, maxBlockX, maxBlockY, maxBlockZ);
        renderBoundingBoxes(mc, greenuppercolor, blueuppercolor);
        
        //GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        GL11.glDepthMask(true);
        GL11.glDepthFunc(GL11.GL_LESS);
        GL11.glLineWidth(3F);
        renderColouredBoundingBox(mc, 0.8F, 0.2F, 0.2F, 1.0f, minBlockX, minBlockY, minBlockZ, maxBlockX, maxBlockY, maxBlockZ);
        renderColoredGridSurface(mc, 0.8F, 0.2F, 0.2F, 0.2f, minBlockX, minBlockY, minBlockZ, maxBlockX, maxBlockY, maxBlockZ);
        renderBoundingBoxes(mc, greenlowercolor, bluelowercolor);
        
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        
        
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
        
        
        
    }

    private static void RecalcBlockMinMax() {
        minBlockX = 0x1e84800;
        minBlockY = 0x1e84800;
        minBlockZ = 0x1e84800;
        maxBlockX = 0xfe17b800;
        maxBlockY = 0xfe17b800;
        maxBlockZ = 0xfe17b800;

        for(int index = 0; index < numHighlights; index++) {
        	if (!testBlockActive[index])
        		continue;
            if(testBlockX[index] + 1 > maxBlockX)
                maxBlockX = testBlockX[index] + 1;

            if(testBlockX[index] < minBlockX)
                minBlockX = testBlockX[index];

            if(testBlockY[index] + 1 > maxBlockY)
                maxBlockY = testBlockY[index] + 1;

            if(testBlockY[index] < minBlockY)
                minBlockY = testBlockY[index];

            if(testBlockZ[index] + 1 > maxBlockZ)
                maxBlockZ = testBlockZ[index] + 1;

            if(testBlockZ[index] < minBlockZ)
                minBlockZ = testBlockZ[index];
        }
    }

    //public static void SetHighlight
    
    public static void RegisterHighlight(int id, int x, int y, int z) {
        if(id < testBlockX.length) {
            testBlockX[id] = x;
            testBlockY[id] = y;
            testBlockZ[id] = z;
            if(id>=numHighlights)
            	numHighlights=id+1;
            testBlockActive[id]=true;
            RecalcBlockMinMax();
        }
    }

    public static void UnregisterHighlight(int x, int y, int z) {
        for(int index = 0; index < numHighlights; index++)
            if(testBlockX[index] == x && testBlockY[index] == y && testBlockZ[index] == z) {
                if(index == numHighlights-1)
                	numHighlights--;

                /*for(int index2 = index; index2 < numHighlights; index2++) {
                    testBlockX[index2] = testBlockX[index2 + 1];
                    testBlockY[index2] = testBlockY[index2 + 1];
                    testBlockZ[index2] = testBlockZ[index2 + 1];
                }*/

                testBlockActive[index]=true;
                RecalcBlockMinMax();
                //return;
            }
    }
    
    public static void UnregisterHighlight(int id) {
    	if(id < testBlockX.length) {
    		if(id == numHighlights-1)
            	numHighlights--;
             testBlockActive[id]=false;
             RecalcBlockMinMax();
         }
    }
    
    public static SelectionType curSelectionType = SelectionType.cuboid;
    public enum SelectionType {
    	none,
    	cuboid,
    	polygon2d
    	;
    }
    
	public static void reinit() {
		cuiInitialized = false;
	}
	
    public static Pattern commandpattern = Pattern.compile("\u00a75\u00a76\u00a74\u00a75([^|]*)\\|?(.*)");
	public static int regionSize=-1;
   // public static Pattern cuboidpattern = Pattern.compile("|([^|]*)|");
    private static boolean cuiInitialized = false;
	public static boolean processChat(String chat) {
		if(!cuiInitialized) {
			Yiffcraft.SendPacket(new Packet3Chat("/worldedit cui"));
			cuiInitialized = true;
		}
	
		Matcher matcher = commandpattern.matcher(chat);
		//debug
		//System.out.println("message: "+chat);
		if(matcher.find())
		{ 
			//debug
			//System.out.println("'"+matcher.group(1)+"'  '"+matcher.group(2)+"'");
			
			//handshake
			if(matcher.group(1).equals(""))
			{
				/*if(Yiffcraft.minecraft.isMultiplayerWorld())
					Yiffcraft.SendPacket(new Packet3Chat("/worldedit cui"));
				else
					System.out.println("/worldedit cui");*/
			}
			//shape
			else
			{
				return handleEvent(matcher.group(1), matcher.group(2).split("\\|"));
			}
			return true;
		}
		return false;
	}

	public static boolean handleEvent(String type, String[] params) 
	{
		System.out.print("CUI event: "+type+" ");
		for (int i=0; i<params.length; i++)
		{
			System.out.print(params[i]+" ");
		}
		System.out.println();
		if(type.equals("s"))
		{
			if (params.length == 0)
			{
				return false;
			}
			if(params[0].equals("cuboid"))
			{
				curSelectionType = SelectionType.cuboid;
			}
			else if(params[0].equals("polygon2d"))
			{
				curSelectionType = SelectionType.polygon2d;
			}
			for(int index = 0; index < numHighlights; index++)
			{
				testBlockActive[index] = false;
			}
			numHighlights=0;
			RecalcBlockMinMax();
			return true;
		}
		//point
		else if(type.equals("p"))
		{
			if (params.length == 0)
			{
				return false;
			}
			
			int id=(int)Float.parseFloat(params[0]);
			RegisterHighlight(id, (int)Float.parseFloat(params[1]), (int)Float.parseFloat(params[2]), (int)Float.parseFloat(params[3]));
			regionSize = (int)Float.parseFloat(params[4]);
			return true;
		}
		else
		{
			return false;
		}
		
	}

}