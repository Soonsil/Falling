package com.example.jongmin.falling.Physics;

import com.example.jongmin.falling.Util.Vector2D;

/**
 * Created by avantgarde on 2017-01-15.
 */

public class Body2D {
    public float mass;
    public float momentOfInertia;

    // linear components
    public Vector2D position;
    public Vector2D velocity;
    public Vector2D acceleration;

    // angular components
    public float orientation;
    public float angularVelocity;
    public float torque;

    public Body2D(float mass, float momentOfInertia, float posX, float posY) {
        this.mass = mass;
        this.momentOfInertia = momentOfInertia;

        this.position = new Vector2D(posX, posY);
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);

        this.orientation = 0;
        this.angularVelocity = 0;
        this.torque = 0;
    }

    public void applyForce(Vector2D force) {
        acceleration.add(force);
    }

    public void applyImpulse(Vector2D impulse, Vector2D contact) {
        velocity.add(Vector2D.scale(impulse, 1.0f / mass));
        angularVelocity += Vector2D.cross(contact, impulse) / momentOfInertia;
    }

    public void integrateForces(float dt) {
        velocity.add(Vector2D.scale(acceleration, 1.0f / mass * dt));
        angularVelocity += torque / momentOfInertia * dt;
    }

    public void integrateVelocity(float dt) {
        position.add(Vector2D.scale(velocity, dt));
        orientation += angularVelocity * dt;
        integrateForces(dt);
    }

    public void clear() {
        acceleration.x = 0;
        acceleration.y = 0;
        torque = 0;
    }
}
