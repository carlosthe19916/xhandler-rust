/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package e2e.renderer.invoice;

import static e2e.renderer.XMLAssertUtils.assertSendSunat;
import static e2e.renderer.XMLAssertUtils.assertSnapshot;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Contacto;
import io.github.project.openubl.xbuilder.content.models.common.Direccion;
import io.github.project.openubl.xbuilder.content.models.common.Firmante;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.ContentEnricher;
import io.github.project.openubl.xbuilder.renderer.TemplateProducer;
import io.quarkus.qute.Template;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import org.junit.jupiter.api.Test;

public class InvoiceTest extends AbstractTest {

    @Test
    public void testInvoiceWithCustomUnidadMedida() throws Exception {
        // Given
        Invoice input = Invoice
            .builder()
            .serie("F001")
            .numero(1)
            .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Softgreen S.A.C.").build())
            .cliente(
                Cliente
                    .builder()
                    .nombre("Carlos Feria")
                    .numeroDocumentoIdentidad("12121212121")
                    .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item1")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .unidadMedida("KGM")
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item2")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .unidadMedida("KGM")
                    .build()
            )
            .build();

        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(input).render();

        // Then
        assertSnapshot(xml, getClass(), "customUnidadMedida.xml");
        assertSendSunat(xml);
    }

    @Test
    public void testInvoiceWithCustomFechaEmision() throws Exception {
        LocalDate fechaEmision = LocalDate.of(2019, Month.JANUARY, 6);
        LocalTime horaEmision = LocalTime.of(0, 0);

        // Given
        Invoice input = Invoice
            .builder()
            .serie("F001")
            .numero(1)
            .fechaEmision(fechaEmision)
            .horaEmision(horaEmision)
            .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Softgreen S.A.C.").build())
            .cliente(
                Cliente
                    .builder()
                    .nombre("Carlos Feria")
                    .numeroDocumentoIdentidad("12121212121")
                    .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item1")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item2")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .build()
            )
            .build();

        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(input).render();

        // Then
        assertSnapshot(xml, getClass(), "customFechaEmision.xml");
        assertSendSunat(xml);
    }

    @Test
    public void testInvoiceWithCustomClienteDireccionAndContacto() throws Exception {
        // Given
        Invoice input = Invoice
            .builder()
            .serie("F001")
            .numero(1)
            .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Softgreen S.A.C.").build())
            .cliente(
                Cliente
                    .builder()
                    .nombre("Carlos Feria")
                    .numeroDocumentoIdentidad("12121212121")
                    .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                    .contacto(Contacto.builder().email("carlos@gmail.com").telefono("+123456789").build())
                    .direccion(
                        Direccion
                            .builder()
                            .ubigeo("050101")
                            .departamento("Ayacucho")
                            .provincia("Huamanga")
                            .distrito("Jesus Nazareno")
                            .codigoLocal("0101")
                            .urbanizacion("000000")
                            .direccion("Jr. Las piedras 123")
                            .codigoPais("PE")
                            .build()
                    )
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item1")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item2")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .build()
            )
            .build();

        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(input).render();

        // Then
        assertSnapshot(xml, getClass(), "customClienteDireccionAndContacto.xml");
        assertSendSunat(xml);
    }

    @Test
    public void testInvoiceWithCustomProveedorDireccionAndContacto() throws Exception {
        // Given
        Invoice input = Invoice
            .builder()
            .serie("F001")
            .numero(1)
            .proveedor(
                Proveedor
                    .builder()
                    .ruc("12345678912")
                    .razonSocial("Softgreen S.A.C.")
                    .contacto(Contacto.builder().email("carlos@gmail.com").telefono("+123456789").build())
                    .direccion(
                        Direccion
                            .builder()
                            .ubigeo("050101")
                            .departamento("Ayacucho")
                            .provincia("Huamanga")
                            .distrito("Jesus Nazareno")
                            .codigoLocal("0101")
                            .urbanizacion("000000")
                            .direccion("Jr. Las piedras 123")
                            .codigoPais("PE")
                            .build()
                    )
                    .build()
            )
            .cliente(
                Cliente
                    .builder()
                    .nombre("Carlos Feria")
                    .numeroDocumentoIdentidad("12121212121")
                    .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item1")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item2")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .build()
            )
            .build();

        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(input).render();

        // Then
        assertSnapshot(xml, getClass(), "customProveedorDireccionAndContacto.xml");
        assertSendSunat(xml);
    }

    @Test
    public void testInvoiceWithCustomFirmante() throws Exception {
        // Given
        Invoice input = Invoice
            .builder()
            .serie("F001")
            .numero(1)
            .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Softgreen S.A.C.").build())
            .cliente(
                Cliente
                    .builder()
                    .nombre("Carlos Feria")
                    .numeroDocumentoIdentidad("12121212121")
                    .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                    .build()
            )
            .firmante(Firmante.builder().ruc("000000000000").razonSocial("Wolsnut4 S.A.C.").build())
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item1")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item2")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .build()
            )
            .build();

        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(input).render();

        // Then
        assertSnapshot(xml, getClass(), "customFirmante.xml");
        assertSendSunat(xml);
    }

    @Test
    public void testInvoiceWithICB_precioUnitario() throws Exception {
        // Given
        Invoice input = Invoice
            .builder()
            .serie("F001")
            .numero(1)
            .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Softgreen S.A.C.").build())
            .cliente(
                Cliente
                    .builder()
                    .nombre("Carlos Feria")
                    .numeroDocumentoIdentidad("12121212121")
                    .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item1")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .icbAplica(true)
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item2")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("100"))
                    .icbAplica(true)
                    .build()
            )
            .build();

        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(input).render();

        // Then
        assertSnapshot(xml, getClass(), "icb.xml");
        assertSendSunat(xml);
    }

    @Test
    public void testInvoiceWithICB_precioConIgv() throws Exception {
        // Given
        Invoice input = Invoice
            .builder()
            .serie("F001")
            .numero(1)
            .proveedor(Proveedor.builder().ruc("12345678912").razonSocial("Softgreen S.A.C.").build())
            .cliente(
                Cliente
                    .builder()
                    .nombre("Carlos Feria")
                    .numeroDocumentoIdentidad("12121212121")
                    .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item1")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("118"))
                    .precioConImpuestos(true)
                    .icbAplica(true)
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item2")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("118"))
                    .precioConImpuestos(true)
                    .icbAplica(true)
                    .build()
            )
            .build();

        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(input).render();

        // Then
        assertSnapshot(xml, getClass(), "icb.xml");
        assertSendSunat(xml);
    }

    @Test
    public void testInvoiceWithCustomProveedor_direccionNotNullAndCodigoLocalNull() throws Exception {
        // Given
        Invoice input = Invoice
            .builder()
            .serie("F001")
            .numero(1)
            .proveedor(
                Proveedor
                    .builder()
                    .ruc("12345678912")
                    .razonSocial("Softgreen S.A.C.")
                    .direccion(Direccion.builder().direccion("Jr. las flores 123").build())
                    .build()
            )
            .cliente(
                Cliente
                    .builder()
                    .nombre("Carlos Feria")
                    .numeroDocumentoIdentidad("12121212121")
                    .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item1")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("118"))
                    .build()
            )
            .detalle(
                DocumentoDetalle
                    .builder()
                    .descripcion("Item2")
                    .cantidad(new BigDecimal("10"))
                    .precio(new BigDecimal("118"))
                    .build()
            )
            .build();

        ContentEnricher enricher = new ContentEnricher(defaults, dateProvider);
        enricher.enrich(input);

        // When
        Template template = TemplateProducer.getInstance().getInvoice();
        String xml = template.data(input).render();

        // Then
        assertSnapshot(xml, getClass(), "customCodigoLocal.xml");
        assertSendSunat(xml);
    }
}