package loon.action.sprite.effect;

import loon.LObject;
import loon.LTexture;
import loon.LTextures;
import loon.action.sprite.ISprite;
import loon.geom.RectBox;
import loon.opengl.GLEx;
import loon.utils.timer.LTimer;

/**
 * 0.3.2起新增类，百叶窗特效 0--竖屏,1--横屏
 */
public class CrossEffect extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int width, height;

	private boolean visible, complete;

	private LTexture otexture, ntexture;

	private LTimer timer;

	private int count, code;

	private int maxcount = 16;

	private int part;

	private int left;

	private int right;

	private LTexture tmp;

	public CrossEffect(int c, String fileName) {
		this(c, LTextures.loadTexture(fileName));
	}

	public CrossEffect(int c, String file1, String file2) {
		this(c, LTextures.loadTexture(file1), LTextures.loadTexture(file2));
	}

	public CrossEffect(int c, LTexture o) {
		this(c, o, null);
	}

	public CrossEffect(int c, LTexture o, LTexture n) {
		this.code = c;
		this.otexture = o;
		this.ntexture = n;
		this.width = (int) o.width();
		this.height = (int) o.height();
		if (width > height) {
			maxcount = 16;
		} else {
			maxcount = 8;
		}
		this.timer = new LTimer(160);
		this.visible = true;
	}

	public void setDelay(long delay) {
		timer.setDelay(delay);
	}

	public long getDelay() {
		return timer.getDelay();
	}

	public boolean isCompleted() {
		return complete;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void update(long elapsedTime) {
		if (complete) {
			return;
		}
		if (this.count > this.maxcount) {
			this.complete = true;
		}
		if (timer.action(elapsedTime)) {
			count++;
		}
	}

	public void createUI(GLEx g) {
		if (!visible) {
			return;
		}
		if (complete) {
			if (ntexture != null) {
				if (alpha > 0 && alpha < 1) {
					g.setAlpha(alpha);
				}
				g.draw(ntexture, x(), y());
				if (alpha != 1f) {
					g.setAlpha(1f);
				}
			}
			return;
		}
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(alpha);
		}
		part = 0;
		left = 0;
		right = 0;
		tmp = null;
		switch (code) {
		default:
			part = width / this.maxcount / 2;
			for (int i = 0; i <= this.maxcount; i++) {
				if (i <= this.count) {
					tmp = this.ntexture;
					if (tmp == null) {
						continue;
					}
				} else {
					tmp = this.otexture;
				}
				left = i * 2 * part;
				right = width - ((i + 1) * 2 - 1) * part;
				g.draw(tmp, x() + left, y(), part, height, left, 0,
						left + part, height);
				g.draw(tmp, x() + right, y(), part, height, right, 0, right
						+ part, height);
			}
			break;
		case 1:
			part = height / this.maxcount / 2;
			for (int i = 0; i <= this.maxcount; i++) {
				if (i <= this.count) {
					tmp = this.ntexture;
					if (tmp == null) {
						continue;
					}
				} else {
					tmp = this.otexture;
				}
				int up = i * 2 * part;
				int down = height - ((i + 1) * 2 - 1) * part;
				g.draw(tmp, 0, up, width, part, 0, up, width, up + part);
				g.draw(tmp, 0, down, width, part, 0, down, width, down + part);
			}
			break;
		}
		if (alpha != 1f) {
			g.setAlpha(1f);
		}
	}

	public void reset() {
		this.complete = false;
		this.count = 0;
	}

	public LTexture getBitmap() {
		return otexture;
	}

	public RectBox getCollisionBox() {
		return getRect(x(), y(), width, height);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getMaxCount() {
		return maxcount;
	}

	public void setMaxCount(int maxcount) {
		this.maxcount = maxcount;
	}

	public void close() {
		if (otexture != null) {
			otexture.close();
			otexture = null;
		}
		if (ntexture != null) {
			ntexture.close();
			ntexture = null;
		}
	}

}