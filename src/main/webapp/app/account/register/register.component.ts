import { Component, AfterViewInit, ElementRef, ViewChild, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/config/error.constants';
import { RegisterService } from './register.service';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../../login/login.service';
import { Login } from '../../login/login.model';

import { TipoConta } from '../../entities/enumerations/tipo-conta.model';
import { PerfilService } from '../../entities/perfil/service/perfil.service';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.scss'],
})
export class RegisterComponent implements AfterViewInit, OnInit {
  tipoConta = TipoConta.MOTORISTA;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;
  acceptedTerms = false;

  registerForm = new FormGroup({
    login: new FormControl(''),
    firstName: new FormControl('', { validators: [Validators.required, Validators.maxLength(50)] }),
    lastName: new FormControl('', { validators: [Validators.required, Validators.maxLength(50)] }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    telephoneNumber: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(11), Validators.maxLength(15)],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    tipoConta: new FormControl('', {}),
    cpf: new FormControl('', {}),
    cnpj: new FormControl('', {}),
    rua: new FormControl('', {}),
    numero: new FormControl('', {}),
    bairro: new FormControl('', {}),
    cidade: new FormControl('', {}),
    estado: new FormControl('', {}),
    cep: new FormControl('', {}),
    pais: new FormControl('', {}),
    nome: new FormControl('', {}),
    razaosocial: new FormControl('', {}),
    telefoneComercial: new FormControl('', {}),
    acceptedTerm: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  constructor(
    private registerService: RegisterService,
    public router: Router,
    private loginService: LoginService,
    protected activatedRoute: ActivatedRoute,
    private perfilService: PerfilService
  ) {}

  ngAfterViewInit(): void {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tipo }) => {
      this.tipoConta = tipo;
    });
  }

  register(): void {
    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const { password, confirmPassword } = this.registerForm.getRawValue();
    if (password !== confirmPassword) {
      this.doNotMatch = true;
    } else {
      let { login, email, telephoneNumber, firstName, lastName } = this.registerForm.getRawValue();
      login = email;

      const dados = this.registerForm.getRawValue();

      const perfil: any = {};

      perfil.nome = firstName + ' ' + lastName;
      perfil.cep = dados.cep || null;
      perfil.razaosocial = dados.razaosocial || dados.nome;
      perfil.telefoneComercial = dados.telefoneComercial || telephoneNumber;
      perfil.cnpj = dados.cnpj || null;
      perfil.cpf = dados.cpf;
      perfil.cidade = dados.cidade || null;
      perfil.estado = dados.estado || null;
      perfil.numero = dados.numero || null;
      perfil.pais = 'br';
      perfil.tipoConta = this.tipoConta;

      this.registerService.save({ login, email, password, telephoneNumber, langKey: 'pt-br', firstName, lastName, perfil }).subscribe({
        next: () => this.autentication({ username: login!, password: password, rememberMe: true }),
        error: response => this.processError(response),
      });
    }
  }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }

  autentication(login: Login): void {
    this.loginService.login(login).subscribe({
      next: () => {
        if (!this.router.getCurrentNavigation()) {
          this.router.navigate(['']).then();
        }
      },
    });
  }
}
