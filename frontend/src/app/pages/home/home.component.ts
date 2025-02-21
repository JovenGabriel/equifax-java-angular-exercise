import {Component} from '@angular/core';
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {UploadService} from "../../services/upload.service";
import {KeyValuePipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {User} from "../../models/User";
import Swal from "sweetalert2";
import {LoginService} from "../../services/login.service";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    MatIconButton,
    MatIcon,
    MatButton,
    KeyValuePipe,
    NgIf,
    NgForOf,
    NgClass
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  selectedFile: any;
  fileUploadResponse: any;

  constructor(private uploadService: UploadService, private loginService: LoginService) { }
  onFilesSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }


  uploadFile() {
    console.log(this.selectedFile);
    this.uploadService.uploadFile(this.selectedFile).subscribe({
      next: response => {
        console.log('Upload successful:', response);

        Swal.fire({
          title: "Load Successful!",
          text: "Data Loaded Successfully!",
          icon: "success"
        }).then(result => {
          this.fileUploadResponse = response;
        })
      },
      error: error => {
        console.error('Upload failed:', error);
        Swal.fire({
          title: "Load Successful!",
          text: "Error Loading Data!",
          icon: "error"
        })
      }
    });
  }

  logout() {
    this.loginService.logout();
  }
}
