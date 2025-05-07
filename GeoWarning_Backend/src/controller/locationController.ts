import { Request, Response } from "express";
import locationService from "../service/locationService";

export const saveLocation = async (req: Request, res: Response): Promise<void> => {
    try {
        const { latitude, longitude } = req.body;

        if (!latitude || !longitude) {
            res.status(400).json({ message: "Latitude e longitude são obrigatórios" });
            return;
        }
        
        const location = await locationService.saveLocation(latitude, longitude);
        console.log(`Caiu ${latitude} ${longitude} ${location}`)
        res.status(201).json(location);
    } catch (error: any) {
        res.status(500).json({ message: error.message });
    }
};

export const listLocations = async (req: Request, res: Response): Promise<void> => {
    try {
        const locations = await locationService.listLocations();
        res.status(200).json(locations);
    } catch (error: any) {
        res.status(500).json({ message: error.message });
    }
};
