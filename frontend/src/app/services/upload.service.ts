import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  constructor(private http: HttpClient) { }

  uploadFile(file: any) {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(
      environment.apiUrl + 'users/import',
      formData
    );
  }
}
