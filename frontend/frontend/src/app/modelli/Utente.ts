import { UtenteMiglioramento } from "./UtenteMiglioramento"
import { UtenteSkin } from "./UtenteSkin"

export interface Utente {
    id: number
    nome: string
    cognome: string
    email: string
    crediti: number
}