//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011-2012, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.particle;

import java.util.List;

/**
 * Contains the basic metadata for an array of particles: position, velocity, scale, rotation,
 * birth time, lifespan.
 */
public class ParticleBuffer
{
    /** The offset of the birth time in the particle buffer. */
    public static final int BIRTH = 0;
    /** The offset of the lifespan in the particle buffer. */
    public static final int LIFESPAN = BIRTH+1;

    /** The offset of the x coordinate of the velocity in the particle buffer. */
    public static final int VEL_X = LIFESPAN+1;
    /** The offset of the y coordinate of the velocity in the particle buffer. */
    public static final int VEL_Y = VEL_X+1;

    /** The offset of the m00 transform element in the particle buffer. */
    public static final int M00 = VEL_Y+1;
    /** The offset of the m01 transform element in the particle buffer. */
    public static final int M01 = M00+1;
    /** The offset of the m10 transform element in the particle buffer. */
    public static final int M10 = M01+1;
    /** The offset of the m11 transform element in the particle buffer. */
    public static final int M11 = M10+1;
    /** The offset of the tx trasform element in the particle buffer. */
    public static final int TX = M11+1;
    /** The offset of the ty trasform element in the particle buffer. */
    public static final int TY = TX+1;

    /** The offset of the red in the particle buffer. */
    public static final int RED = TY+1;
    /** The offset of the green in the particle buffer. */
    public static final int GREEN = RED+1;
    /** The offset of the blue in the particle buffer. */
    public static final int BLUE = GREEN+1;
    /** The offset of the alpha in the particle buffer. */
    public static final int ALPHA = BLUE+1;

    /** The total number of fields per particle. */
    public static final int NUM_FIELDS = ALPHA+1;

    /** The particle data. */
    public final float[] data;

    /** A liveness flag for each particle. */
    public final int[] alive;

    /** Creates a particle buffer that can hold up to {@code maxParticles} particles. */
    public ParticleBuffer (int maxParticles) {
        _maxParticles = maxParticles;
        data = new float[maxParticles * NUM_FIELDS];
        alive = new int[maxParticles/32+1];
    }

    /** Returns true if the specified particle is alive. */
    public boolean isAlive (int partidx) {
        return (alive[partidx/32] & (1 << partidx % 32)) != 0;
    }

    /** Sets the particle in question to alive or not. */
    public void setAlive (int partidx, boolean isAlive) {
        if (isAlive) {
            alive[partidx/32] |= (1 << partidx % 32);
        } else {
            alive[partidx/32] &= ~(1 << partidx % 32);
        }
    }

    /** Adds {@code count} particles to this buffer, and initializes them with {@code initters}. */
    public void add (int count, float now, List<? extends Initializer> initters) {
        // optimization when we're full
        if (_live >= _maxParticles) return;
        // TODO: keep track of a last added position and start from there
        int pp = 0, ppos = 0, icount = initters.size(), initted = 0;
        for (int aa = 0; aa < alive.length && initted < count; aa++) {
            int live = alive[aa], mask = 1;
            if (live == 0xFFFFFFFF) {
                pp += 32;
                ppos += 32*NUM_FIELDS;
                continue; // all full
            }
            for (int end = Math.min(pp+32, _maxParticles); pp < end && initted < count;
                 pp++, ppos += NUM_FIELDS, mask <<= 1) {
                if ((live & mask) != 0) continue;
                live |= mask;
                data[ppos+BIRTH] = now;
                for (int ii = 0; ii < icount; ii++) {
                    initters.get(ii).init(pp, data, ppos);
                }
                initted++;
            }
            alive[aa] = live;
        }
    }

    /**
     * Applies the supplied effectors to all (live) particles in this buffer.
     *
     * @return the number of live particles to which the effectors were applied.
     */
    public int apply (List<? extends Effector> effectors, float now, float dt) {
        int pp = 0, ppos = 0, ecount = effectors.size(), death = 0, living = 0;
        for (int aa = 0; aa < alive.length; aa++) {
            int live = alive[aa], mask = 1, died = 0;
            for (int end = pp+32; pp < end; pp++, ppos += NUM_FIELDS, mask <<= 1) {
                // if this particle is not alive, skip it
                if ((live & mask) == 0) continue;

                // if this particle has died, mark it as such
                if (now - data[ppos+BIRTH] > data[ppos+LIFESPAN]) {
                    live &= ~mask;
                    died++;
                    continue;
                }

                // the particle lives, apply the effectors
                for (int ee = 0; ee < ecount; ee++)
                    effectors.get(ee).apply(pp, data, ppos, now, dt);
                living++;
            }

            // if we killed off any particles, update the liveness array
            if (died > 0) {
                alive[aa] = live;
                death += died;
            }
        }
        return living;
    }

    /** Renders the particles to the supplied shader. */
    public void render (ParticleShader shader, float width, float height) {
        float ql = -width/2, qt = -height/2, qr = width/2, qb = height/2;
        int pp = 0, ppos = 0, rendered = 0;
        for (int aa = 0; aa < alive.length; aa++) {
            int live = alive[aa], mask = 1;
            for (int end = pp+32; pp < end; pp++, ppos += NUM_FIELDS, mask <<= 1) {
                if ((live & mask) == 0) continue;
                shader.core.addQuad(ql, qt, qr, qb, data, ppos);
                rendered++;
            }
        }
        _live = rendered;
    }

    protected final int _maxParticles;
    protected int _live;
}
