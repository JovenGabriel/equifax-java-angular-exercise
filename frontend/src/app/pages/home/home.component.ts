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


  /**
   * Handles file upload functionality. It sends the selected file to the upload service
   * and processes the response or error messages accordingly. Displays alerts for success
   * or failure of the file upload process.
   *
   * @return {void} This method does not return a value.
   */
  uploadFile() {
    this.uploadService.uploadFile(this.selectedFile).subscribe({
      next: response => {
        Swal.fire({
          title: "Load Successful!",
          text: "Data Loaded Successfully!",
          icon: "success"
        }).then(result => {
          this.fileUploadResponse = response;
        })
      },
      error: error => {
        Swal.fire({
          title: "Error!",
          text: "Error Loading Data!",
          icon: "error"
        })
      }
    });
  }

  /**
   * Logs the user out by invoking the logout service.
   *
   * @return {void} Does not return a value.
   */
  logout() {
    this.loginService.logout();
  }
}
