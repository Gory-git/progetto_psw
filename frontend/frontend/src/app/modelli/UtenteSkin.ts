import { Skin } from "./Skin"
import { Utente } from "./Utente"

export interface UtenteSkin {
    id: number
    utente: Utente
    skin: Skin
}