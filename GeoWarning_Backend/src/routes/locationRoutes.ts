import { Router } from 'express';
import { saveLocation, listLocations } from '../controller/locationController';

const router = Router();

router.post('/saveLocation', saveLocation);
router.get('/locations', listLocations);

export default router;
