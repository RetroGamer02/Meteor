// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) nonlb 

import net.runelite.mapping.Implements;

import java.awt.Component;

@Implements("RSSpriteSurface")
public final class SpriteSurface extends Surface {

	public void spriteClipping(int x, int y, int width, int height, int id, int tX, int tY) {
		if(id >= 50000) {
			Client.mudClient.drawTeleportBubble(x, y, width, height, id - 50000, tX, tY);
			return;
		}
		if(id >= 40000) {
			Client.mudClient.drawItem(x, y, width, height, id - 40000, tX, tY);
			return;
		}
		if(id >= 20000) {
			Client.mudClient.drawNPC(x, y, width, height, id - 20000, tX, tY);
			return;
		}
		if(id >= 5000) {
			Client.mudClient.drawPlayer(x, y, width, height, id - 5000, tX, tY);
        } else {
			super.spriteClipping(x, y, width, height, id);
        }
	}

	public SpriteSurface(int width, int height, int k, Component component) {
		super(width, height, k, component);
		boolean flag = false;
		if(flag)
			new Surface(width, height, k, component);
	}
}
