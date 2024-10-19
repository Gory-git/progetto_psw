import { UtenteMiglioramento } from "./UtenteMiglioramento"
import { UtenteSkin } from "./UtenteSkin"

export interface Utente {
    id: number
    nome: string
    cognome: string
    email: string
    password: string
    crediti: number
    miglioramenti: any
    skin: any
}