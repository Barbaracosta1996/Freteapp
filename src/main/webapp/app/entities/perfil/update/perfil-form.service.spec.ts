import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../perfil.test-samples';

import { PerfilFormService } from './perfil-form.service';

describe('Perfil Form Service', () => {
  let service: PerfilFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PerfilFormService);
  });

  describe('Service methods', () => {
    describe('createPerfilFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPerfilFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoConta: expect.any(Object),
            cpf: expect.any(Object),
            cnpj: expect.any(Object),
            rua: expect.any(Object),
            numero: expect.any(Object),
            bairro: expect.any(Object),
            cidade: expect.any(Object),
            estado: expect.any(Object),
            cep: expect.any(Object),
            pais: expect.any(Object),
            nome: expect.any(Object),
            razaosocial: expect.any(Object),
            telefoneComercial: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IPerfil should create a new form with FormGroup', () => {
        const formGroup = service.createPerfilFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoConta: expect.any(Object),
            cpf: expect.any(Object),
            cnpj: expect.any(Object),
            rua: expect.any(Object),
            numero: expect.any(Object),
            bairro: expect.any(Object),
            cidade: expect.any(Object),
            estado: expect.any(Object),
            cep: expect.any(Object),
            pais: expect.any(Object),
            nome: expect.any(Object),
            razaosocial: expect.any(Object),
            telefoneComercial: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getPerfil', () => {
      it('should return NewPerfil for default Perfil initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPerfilFormGroup(sampleWithNewData);

        const perfil = service.getPerfil(formGroup) as any;

        expect(perfil).toMatchObject(sampleWithNewData);
      });

      it('should return NewPerfil for empty Perfil initial value', () => {
        const formGroup = service.createPerfilFormGroup();

        const perfil = service.getPerfil(formGroup) as any;

        expect(perfil).toMatchObject({});
      });

      it('should return IPerfil', () => {
        const formGroup = service.createPerfilFormGroup(sampleWithRequiredData);

        const perfil = service.getPerfil(formGroup) as any;

        expect(perfil).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPerfil should not enable id FormControl', () => {
        const formGroup = service.createPerfilFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPerfil should disable id FormControl', () => {
        const formGroup = service.createPerfilFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
