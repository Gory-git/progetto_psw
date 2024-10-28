import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Miglioramento } from "../modelli/Miglioramento";
import { OAuthService } from "angular-oauth2-oidc";

const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type':  'application/json',
    })
};

@Injectable({
    providedIn: 'root'
})
export class MiglioramentiService {
    private url = 'http://localhost:8080/miglioramenti'

    constructor(private oauthService: OAuthService,private httpClient: HttpClient) {}
    
    all(): Observable<any> {
        return this.httpClient.get<any>(this.url+'/all', {
            headers: { 
              'Authorization': `Bearer ${this.oauthService.getAccessToken()}`
            }
          })
    }

    nome(params: HttpParams): Observable<any> {
        return this.httpClient.get<any>(this.url, {    
            headers: { 
              'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
            },
            params: params
          })
    }

    crediti(params: HttpParams): Observable<any> {
        return this.httpClient.get<any>(this.url + params.get('crediti'), {   
            headers: { 
              'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
            },
            params: params 
          })
    }

    acquire(params: HttpParams): Observable<any> {
      return this.httpClient.post<any>(this.url+'/acquire', null, {
          headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
          },
          params: params
      })
    }

add(params: any) {
  return this.httpClient.post<any>(this.url+'/save', null, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
      },
      params : params
    })
  }
}