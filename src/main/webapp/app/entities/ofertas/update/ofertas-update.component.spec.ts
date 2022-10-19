import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OfertasFormService } from './ofertas-form.service';
import { OfertasService } from '../service/ofertas.service';
import { IOfertas } from '../ofertas.model';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { PerfilService } from 'app/entities/perfil/service/perfil.service';

import { OfertasUpdateComponent } from './ofertas-update.component';

describe('Ofertas Management Update Component', () => {
  let comp: OfertasUpdateComponent;
  let fixture: ComponentFixture<OfertasUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ofertasFormService: OfertasFormService;
  let ofertasService: OfertasService;
  let perfilService: PerfilService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OfertasUpdateComponent],
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
      .overrideTemplate(OfertasUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OfertasUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ofertasFormService = TestBed.inject(OfertasFormService);
    ofertasService = TestBed.inject(OfertasService);
    perfilService = TestBed.inject(PerfilService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Perfil query and add missing value', () => {
      const ofertas: IOfertas = { id: 456 };
      const perfil: IPerfil = { id: 18632 };
      ofertas.perfil = perfil;

      const perfilCollection: IPerfil[] = [{ id: 58040 }];
      jest.spyOn(perfilService, 'query').mockReturnValue(of(new HttpResponse({ body: perfilCollection })));
      const additionalPerfils = [perfil];
      const expectedCollection: IPerfil[] = [...additionalPerfils, ...perfilCollection];
      jest.spyOn(perfilService, 'addPerfilToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ofertas });
      comp.ngOnInit();

      expect(perfilService.query).toHaveBeenCalled();
      expect(perfilService.addPerfilToCollectionIfMissing).toHaveBeenCalledWith(
        perfilCollection,
        ...additionalPerfils.map(expect.objectContaining)
      );
      expect(comp.perfilsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ofertas: IOfertas = { id: 456 };
      const perfil: IPerfil = { id: 45707 };
      ofertas.perfil = perfil;

      activatedRoute.data = of({ ofertas });
      comp.ngOnInit();

      expect(comp.perfilsSharedCollection).toContain(perfil);
      expect(comp.ofertas).toEqual(ofertas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOfertas>>();
      const ofertas = { id: 123 };
      jest.spyOn(ofertasFormService, 'getOfertas').mockReturnValue(ofertas);
      jest.spyOn(ofertasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ofertas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ofertas }));
      saveSubject.complete();

      // THEN
      expect(ofertasFormService.getOfertas).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ofertasService.update).toHaveBeenCalledWith(expect.objectContaining(ofertas));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOfertas>>();
      const ofertas = { id: 123 };
      jest.spyOn(ofertasFormService, 'getOfertas').mockReturnValue({ id: null });
      jest.spyOn(ofertasService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ofertas: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ofertas }));
      saveSubject.complete();

      // THEN
      expect(ofertasFormService.getOfertas).toHaveBeenCalled();
      expect(ofertasService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOfertas>>();
      const ofertas = { id: 123 };
      jest.spyOn(ofertasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ofertas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ofertasService.update).toHaveBeenCalled();
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
