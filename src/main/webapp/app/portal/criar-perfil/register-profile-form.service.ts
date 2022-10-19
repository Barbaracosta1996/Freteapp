import { Injectable } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {IPerfil, NewPerfil} from "../../entities/perfil/perfil.model";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPerfil for edit and NewPerfilFormGroupInput for create.
 */
type ProfileFormGroupInput = IPerfil | PartialWithRequiredKeyOf<NewPerfil>;

type ProfileFormDefaults = Pick<NewPerfil, 'id'>;

type ProfileFormGroupContent = {
  id: FormControl<IPerfil['id'] | NewPerfil['id']>;
  tipoConta: FormControl<IPerfil['tipoConta']>;
  cpf: FormControl<IPerfil['cpf']>;
  cnpj: FormControl<IPerfil['cnpj']>;
  rua: FormControl<IPerfil['rua']>;
  numero: FormControl<IPerfil['numero']>;
  bairro: FormControl<IPerfil['bairro']>;
  cidade: FormControl<IPerfil['cidade']>;
  estado: FormControl<IPerfil['estado']>;
  cep: FormControl<IPerfil['cep']>;
  pais: FormControl<IPerfil['pais']>;
  nome: FormControl<IPerfil['nome']>;
  razaosocial: FormControl<IPerfil['razaosocial']>;
  telefoneComercial: FormControl<IPerfil['telefoneComercial']>;
};

export type ProfileFormGroup = FormGroup<ProfileFormGroupContent>;

@Injectable({
  providedIn: 'root'
})
export class RegisterProfileFormService {
  createProfileFormGroup(perfil: ProfileFormGroupInput = { id: null }): ProfileFormGroup {
    const profileRawGroup = {
      ...this.getFormDefaults(),
      ...perfil,
    };
    return new FormGroup<ProfileFormGroupContent>({
      id: new FormControl(
        { value: profileRawGroup.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tipoConta: new FormControl(profileRawGroup.tipoConta),
      cpf: new FormControl(profileRawGroup.cpf, {
        validators: [Validators.required, Validators.minLength(11)],
      }),
      cnpj: new FormControl(profileRawGroup.cnpj, {
        validators: [Validators.minLength(14)],
      }),
      rua: new FormControl(profileRawGroup.rua),
      numero: new FormControl(profileRawGroup.numero),
      bairro: new FormControl(profileRawGroup.bairro),
      cidade: new FormControl(profileRawGroup.cidade),
      estado: new FormControl(profileRawGroup.estado),
      cep: new FormControl(profileRawGroup.cep),
      pais: new FormControl(profileRawGroup.pais),
      nome: new FormControl(profileRawGroup.nome, {
        validators: [Validators.required],
      }),
      razaosocial: new FormControl(profileRawGroup.razaosocial),
      telefoneComercial: new FormControl(profileRawGroup.telefoneComercial)
    });
  }

  createProfileFormGroupCompany(perfil: ProfileFormGroupInput = { id: null }): ProfileFormGroup {
    const profileRawGroup = {
      ...this.getFormDefaults(),
      ...perfil,
    };
    return new FormGroup<ProfileFormGroupContent>({
      id: new FormControl(
        { value: profileRawGroup.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tipoConta: new FormControl(profileRawGroup.tipoConta),
      cpf: new FormControl(profileRawGroup.cpf, {
        validators: [Validators.minLength(11)],
      }),
      cnpj: new FormControl(profileRawGroup.cnpj, {
        validators: [Validators.required, Validators.minLength(14)],
      }),
      rua: new FormControl(profileRawGroup.rua),
      numero: new FormControl(profileRawGroup.numero),
      bairro: new FormControl(profileRawGroup.bairro),
      cidade: new FormControl(profileRawGroup.cidade),
      estado: new FormControl(profileRawGroup.estado),
      cep: new FormControl(profileRawGroup.cep),
      pais: new FormControl(profileRawGroup.pais),
      nome: new FormControl(profileRawGroup.nome, {
        validators: [Validators.required],
      }),
      razaosocial: new FormControl(profileRawGroup.razaosocial, {
        validators: [Validators.required],
      }),
      telefoneComercial: new FormControl(profileRawGroup.telefoneComercial)
    });
  }

  getPerfil(form: ProfileFormGroup): IPerfil {
    return form.getRawValue() as IPerfil;
  }

  resetForm(form: ProfileFormGroup, perfil: ProfileFormGroupInput): void {
    const perfilRawValue = { ...this.getFormDefaults(), ...perfil };
    form.reset(
      {
        ...perfilRawValue,
        id: { value: perfilRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProfileFormDefaults {
    return {
      id: null,
    };
  }
}
