import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PerfilAnexosFormService } from './perfil-anexos-form.service';
import { PerfilAnexosService } from '../service/perfil-anexos.service';
import { IPerfilAnexos } from '../perfil-anexos.model';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { PerfilService } from 'app/entities/perfil/service/perfil.service';

import { PerfilAnexosUpdateComponent } from './perfil-anexos-update.component';

describe('PerfilAnexos Management Update Component', () => {
  let comp: PerfilAnexosUpdateComponent;
  let fixture: ComponentFixture<PerfilAnexosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let perfilAnexosFormService: PerfilAnexosFormService;
  let perfilAnexosService: PerfilAnexosService;
  let perfilService: PerfilService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PerfilAnexosUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PerfilAnexosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PerfilAnexosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    perfilAnexosFormService = TestBed.inject(PerfilAnexosFormService);
    perfilAnexosService = TestBed.inject(PerfilAnexosService);
    perfilService = TestBed.inject(PerfilService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Perfil query and add missing value', () => {
      const perfilAnexos: IPerfilAnexos = { id: 456 };
      const perfil: IPerfil = { id: 41415 };
      perfilAnexos.perfil = perfil;

      const perfilCollection: IPerfil[] = [{ id: 95122 }];
      jest.spyOn(perfilService, 'query').mockReturnValue(of(new HttpResponse({ body: perfilCollection })));
      const additionalPerfils = [perfil];
      const expectedCollection: IPerfil[] = [...additionalPerfils, ...perfilCollection];
      jest.spyOn(perfilService, 'addPerfilToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ perfilAnexos });
      comp.ngOnInit();

      expect(perfilService.query).toHaveBeenCalled();
      expect(perfilService.addPerfilToCollectionIfMissing).toHaveBeenCalledWith(
        perfilCollection,
        ...additionalPerfils.map(expect.objectContaining)
      );
      expect(comp.perfilsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const perfilAnexos: IPerfilAnexos = { id: 456 };
      const perfil: IPerfil = { id: 34559 };
      perfilAnexos.perfil = perfil;

      activatedRoute.data = of({ perfilAnexos });
      comp.ngOnInit();

      expect(comp.perfilsSharedCollection).toContain(perfil);
      expect(comp.perfilAnexos).toEqual(perfilAnexos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfilAnexos>>();
      const perfilAnexos = { id: 123 };
      jest.spyOn(perfilAnexosFormService, 'getPerfilAnexos').mockReturnValue(perfilAnexos);
      jest.spyOn(perfilAnexosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfilAnexos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: perfilAnexos }));
      saveSubject.complete();

      // THEN
      expect(perfilAnexosFormService.getPerfilAnexos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(perfilAnexosService.update).toHaveBeenCalledWith(expect.objectContaining(perfilAnexos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfilAnexos>>();
      const perfilAnexos = { id: 123 };
      jest.spyOn(perfilAnexosFormService, 'getPerfilAnexos').mockReturnValue({ id: null });
      jest.spyOn(perfilAnexosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfilAnexos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: perfilAnexos }));
      saveSubject.complete();

      // THEN
      expect(perfilAnexosFormService.getPerfilAnexos).toHaveBeenCalled();
      expect(perfilAnexosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfilAnexos>>();
      const perfilAnexos = { id: 123 };
      jest.spyOn(perfilAnexosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfilAnexos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(perfilAnexosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePerfil', () => {
      it('Should forward to perfilService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(perfilService, 'comparePerfil');
        comp.comparePerfil(entity, entity2);
        expect(perfilService.comparePerfil).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
