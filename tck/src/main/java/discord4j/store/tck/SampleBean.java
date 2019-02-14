/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J. If not, see <http://www.gnu.org/licenses/>.
 */

package discord4j.store.tck;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import reactor.util.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class SampleBean implements Serializable {

    private long id;
    private String fileName;
    private int size;
    private String url;
    private String proxyUrl;
    @Nullable
    private Integer height;
    @Nullable
    private Integer width;

    public SampleBean() {
        this.id = RandomUtils.nextLong();
        this.fileName = RandomStringUtils.randomAlphanumeric(8);
        this.size = RandomUtils.nextInt();
        this.url = RandomStringUtils.randomAlphanumeric(8);
        this.proxyUrl = RandomStringUtils.randomAlphanumeric(8);
        this.height = RandomUtils.nextInt();
        this.width = RandomUtils.nextInt();
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(final String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    @Nullable
    public Integer getHeight() {
        return height;
    }

    public void setHeight(@Nullable final Integer height) {
        this.height = height;
    }

    @Nullable
    public Integer getWidth() {
        return width;
    }

    public void setWidth(@Nullable final Integer width) {
        this.width = width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SampleBean that = (SampleBean) o;
        return id == that.id &&
                size == that.size &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(url, that.url) &&
                Objects.equals(proxyUrl, that.proxyUrl) &&
                Objects.equals(height, that.height) &&
                Objects.equals(width, that.width);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, size, url, proxyUrl, height, width);
    }
}
