import LocationModel from "../models/locationModel";

class LocationRepository {
    async saveLocation(locationData: { latitude: number, longitude: number, timestamp: Date }) {
        try {
            const location = new LocationModel(locationData);
            const savedLocation = await location.save();
            return savedLocation;
        } catch (error) {
            console.error("Erro ao salvar localização:", error);
            throw error;
        }
    }

    async getAllLocations() {
        return await LocationModel.find().sort({ timestamp: -1 });
    }
}

export default new LocationRepository();
