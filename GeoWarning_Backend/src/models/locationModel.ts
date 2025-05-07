import { Schema, model } from 'mongoose';

const locationSchema = new Schema({
    latitude: {
        type: Number,
        required: true
    },
    longitude: {
        type: Number,
        required: true
    },
    timestamp: {
        type: Date,
        required: true
    }
});

const LocationModel = model('Location', locationSchema);

export default LocationModel;
