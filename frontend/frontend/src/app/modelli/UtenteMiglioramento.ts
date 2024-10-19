import { Miglioramento } from "./Miglioramento"
import { Utente } from "./Utente"

export interface UtenteMiglioramento {
    id: number
    utente: Utente
    miglioramento: Miglioramento
    quantita: number
}