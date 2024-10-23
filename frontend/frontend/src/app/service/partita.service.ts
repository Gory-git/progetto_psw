import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { OAuthService } from "angular-oauth2-oidc";

const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type':  'application/json',
    })
};

@Injectable({
    providedIn: 'root'
})

export class PartitaService {
    private url: string = 'http://localhost:8080/game/'

    constructor(private oauthService: OAuthService,private httpClient: HttpClient) {}

    salva(params: HttpParams): Observable<any> {
        return this.httpClient.post<any>(this.url+'save', null, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
            },
            params: params
        })
    }
}