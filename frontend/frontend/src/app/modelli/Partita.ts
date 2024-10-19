import { Utente } from "./Utente"

export interface Partita {
    id: number
    utente: Utente
    crediti_ottenuti: number
    esito: string
    data_partita: any
}