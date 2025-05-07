import locationRepository from "../repository/locationRepository";

class LocationService {
    async saveLocation(latitude: number, longitude: number) {
        try {
            const locationData = {
                latitude: latitude,
                longitude: longitude,
                timestamp: new Date() // você pode adicionar um campo para timestamp
            };

            // Chama o repositório para salvar a localização
            return await locationRepository.saveLocation(locationData);
        } catch (error) {
            console.error("Erro ao salvar localização:", error);
            throw new Error("Erro ao salvar localização");
        }
    }

    async listLocations() {
        return await locationRepository.getAllLocations();
    }
}

export default new LocationService();
