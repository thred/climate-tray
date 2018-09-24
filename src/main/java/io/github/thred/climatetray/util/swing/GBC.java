/*
 * Copyright (c) 2012 swing-on-fire Team
 *
 * This file is part of Swing-On-Fire (http://code.google.com/p/swing-on-fire), licensed under the terms of the MIT
 * License (MIT).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.thred.climatetray.util.swing;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Arrays;

public class GBC extends GridBagConstraints
{

    private static final long serialVersionUID = 4241366948629444861L;

    private static final Insets DEFAULT_INSETS = new Insets(4, 8, 4, 8);
    private static final Insets DEFAULT_OUTSETS = new Insets(4, 8, 4, 8);

    private final int totalWidth;
    private final int totalHeight;

    private Insets defaultInsets = (Insets) DEFAULT_INSETS.clone();
    private Insets defaultOutsets = (Insets) DEFAULT_OUTSETS.clone();

    private boolean[][] grid;
    private boolean insetsOverwritten = false;

    public GBC(int totalWidth)
    {
        this(totalWidth, -1);
    }

    public GBC(int totalWidth, int totalHeight)
    {
        super();

        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;

        grid = new boolean[(totalHeight > 0) ? totalHeight : 1][totalWidth];

        gridx = 1;
        gridy = 1;

        reset();
    }

    protected GBC reset()
    {
        gridwidth = 1;
        gridheight = 1;
        weightx = 0;
        weighty = 0;
        anchor = WEST;
        fill = NONE;

        insetsOverwritten = false;

        insets = (Insets) defaultInsets.clone();

        return this;
    }

    protected GBC resetInsets()
    {
        if (!insetsOverwritten)
        {
            int top = (gridy > 1) ? defaultInsets.top : defaultOutsets.top;
            int left = (gridx > 1) ? defaultInsets.left : defaultOutsets.left;
            int bottom = ((totalHeight <= 0) || ((gridy + gridheight) < totalHeight)) ? defaultInsets.bottom
                : defaultOutsets.bottom;
            int right = ((gridx + gridwidth) < totalWidth) ? defaultInsets.right : defaultOutsets.right;

            insets = new Insets(top, left, bottom, right);
        }

        return this;
    }

    protected boolean isGridUsed()
    {
        return isGridUsed(gridx - 1, gridy - 1, gridwidth, gridheight);
    }

    protected boolean isGridUsed(int x, int y, int width, int height)
    {
        int x2 = Math.min(x + width, totalWidth);
        int y2 = Math.min(y + height, totalHeight);

        for (int j = y; j < y2; j += 1)
        {
            for (int i = x; i < x2; i += 1)
            {
                if (grid[j][i])
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected GBC setGridUsed(boolean used)
    {
        return setGridUsed(gridx - 1, gridy - 1, gridwidth, gridheight, used);
    }

    protected GBC setGridUsed(int x, int y, int width, int height, boolean used)
    {
        int x2 = x + width;
        int y2 = y + height;

        if (x2 > totalWidth)
        {
            throw new IllegalArgumentException(
                String.format("x (%d) + width (%d) > totalWidth (%d)", x, width, totalWidth));
        }

        if ((height > 0) && (y2 > totalHeight))
        {
            throw new IllegalArgumentException(
                String.format("y (%d) + height (%d) > totalHeight (%d)", y, height, totalHeight));
        }

        if (y2 > grid.length)
        {
            int length = grid.length;
            grid = Arrays.copyOf(grid, y2);
            Arrays.fill(grid, length, y2, new boolean[width]);
        }

        for (int j = y; j < y2; j += 1)
        {
            for (int i = x; i < x2; i += 1)
            {
                grid[j][i] = used;
            }
        }

        return this;
    }

    public GBC next()
    {
        do
        {
            gridx += gridwidth;

            if (gridx > totalWidth)
            {
                gridx = 1;
                gridy += 1;
            }

            reset();
        } while (isGridUsed());

        setGridUsed(true);

        return this;
    }

    public GBC span(int gridwidth)
    {
        return span(gridwidth, gridheight);
    }

    public GBC span(int gridwidth, int gridheight)
    {
        setGridUsed(false);

        this.gridwidth = gridwidth;
        this.gridheight = gridheight;

        if (((gridx - 1) + gridwidth) > totalWidth)
        {
            throw new IllegalArgumentException(
                String.format("gridx (%d) + gridwidth (%d) > width (%d)", gridx - 1, gridwidth, totalWidth));
        }

        if ((totalHeight > 0) && (((gridy - 1) + gridheight) > totalHeight))
        {
            throw new IllegalArgumentException(
                String.format("gridy (%d) + gridheight (%d) > height (%d)", gridy - 1, gridheight, totalHeight));
        }

        if (isGridUsed())
        {
            throw new IllegalArgumentException(
                String.format("Overlapping components: %d, %d, %d, %d", gridx - 1, gridy - 1, gridwidth, gridheight));
        }

        setGridUsed(true);
        resetInsets();

        return this;
    }

    public GBC weight(double weightx)
    {
        this.weightx = weightx;

        return this;
    }

    public GBC weight(double weightx, double weighty)
    {
        this.weightx = weightx;
        this.weighty = weighty;

        return this;
    }

    public GBC insetTop(int top)
    {
        return insets(top, insets.left, insets.bottom, insets.right);
    }

    public GBC insetBottom(int bottom)
    {
        return insets(insets.top, insets.left, bottom, insets.right);
    }

    public GBC insetLeft(int left)
    {
        return insets(insets.top, left, insets.bottom, insets.right);
    }

    public GBC insetRight(int right)
    {
        return insets(insets.top, insets.left, insets.bottom, right);
    }

    public GBC insets(int top, int left, int bottom, int right)
    {
        insets = new Insets(top, left, bottom, right);
        insetsOverwritten = true;

        return this;
    }

    public GBC defaultInsets(int top, int left, int bottom, int right)
    {
        defaultInsets = new Insets(top, left, bottom, right);

        return resetInsets();
    }

    public GBC defaultOutsets(int top, int left, int bottom, int right)
    {
        defaultOutsets = new Insets(top, left, bottom, right);

        return resetInsets();
    }

    public GBC center()
    {
        anchor = CENTER;

        return this;
    }

    public GBC left()
    {
        switch (anchor)
        {
            case NORTH:
            case NORTHEAST:
            case NORTHWEST:
                anchor = NORTHWEST;
                break;

            case SOUTH:
            case SOUTHEAST:
            case SOUTHWEST:
                anchor = SOUTHWEST;
                break;

            default:
                anchor = WEST;
                break;
        }

        return this;
    }

    public GBC right()
    {
        switch (anchor)
        {
            case NORTH:
            case NORTHEAST:
            case NORTHWEST:
                anchor = NORTHEAST;
                break;

            case SOUTH:
            case SOUTHEAST:
            case SOUTHWEST:
                anchor = SOUTHEAST;
                break;

            default:
                anchor = EAST;
                break;
        }

        return this;
    }

    public GBC top()
    {
        switch (anchor)
        {
            case EAST:
            case NORTHEAST:
            case SOUTHEAST:
                anchor = NORTHEAST;
                break;

            case WEST:
            case NORTHWEST:
            case SOUTHWEST:
                anchor = NORTHWEST;
                break;

            default:
                anchor = NORTH;
                break;
        }

        return this;
    }

    public GBC bottom()
    {
        switch (anchor)
        {
            case EAST:
            case NORTHEAST:
            case SOUTHEAST:
                anchor = SOUTHEAST;
                break;

            case WEST:
            case NORTHWEST:
            case SOUTHWEST:
                anchor = SOUTHWEST;
                break;

            default:
                anchor = SOUTH;
                break;
        }

        return this;
    }

    public GBC hFill()
    {
        switch (fill)
        {
            case VERTICAL:
            case BOTH:
                fill = BOTH;
                break;

            default:
                fill = HORIZONTAL;
                break;
        }

        return this;
    }

    public GBC vFill()
    {
        switch (fill)
        {
            case HORIZONTAL:
            case BOTH:
                fill = BOTH;
                break;

            default:
                fill = VERTICAL;
                break;
        }

        return this;
    }

    public GBC fill()
    {
        fill = BOTH;

        return this;
    }

}
