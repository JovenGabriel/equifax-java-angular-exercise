import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {environment} from "../../../environments/environment";
import {LoginService} from "../../services/login.service";
import Swal from 'sweetalert2';
import {Router} from "@angular/router";
import {User} from "../../models/User";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    MatFormField,
    MatInput,
    MatButton,
    MatLabel,
    ReactiveFormsModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
    loginForm: FormGroup;

    ngOnInit() {
        if(this.loginService.isLoggedIn()) {
            this.router.navigate(['/home']);
        }
    }

    constructor(private formBuilder: FormBuilder, private loginService: LoginService, private router: Router) {
        this.loginForm = this.formBuilder.group({
            email: new FormControl('', [Validators.required, Validators.email]),
            password: new FormControl('', [Validators.required])
        })
    }

    login() {
        this.loginService.login(this.loginForm.value).subscribe({
            next: response => {
                // @ts-ignore
                this.loginService.setToken(response.token);
                Swal.fire({
                    title: "Login Successful!",
                    text: "Welcome!",
                    icon: "success"
                }).then(result => {
                    this.router.navigate(['/home']);
                })

            },
            error: error => {
                Swal.fire({
                    title: "Login Failed",
                    text: "Bad Credentials!",
                    icon: "error"
                })
            }
        });
    }
}
