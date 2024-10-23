import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { OAuthService } from "angular-oauth2-oidc";
import { Utente } from "../modelli/Utente";

const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type':  'application/json',
    })
};

@Injectable({
    providedIn: 'root'
})

export class UtenteService {
    private url: string = 'http://localhost:8080/user/'

    constructor(private oauthService: OAuthService,private httpClient: HttpClient) {}

    getInfo(): Observable<any> {
        return this.httpClient.get<any>(this.url + 'page', {
            headers: {
                'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
            }})
    }

    salva(): Observable<any> {
        return this.httpClient.post<any>(this.url + 'register', null, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
            }})
    }
}