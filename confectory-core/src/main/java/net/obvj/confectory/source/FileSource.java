package net.obvj.confectory.source;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.util.StringUtils;

/**
 * A specialized configuration source implementation for loading a local file from the
 * file system.
 * <p>
 * The path may contain system environment variables, which must be placed between
 * <code>"${"</code> and <code>"}"</code>. For example: <code>"${TEMP}/file.txt"</code>
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class FileSource<T> extends AbstractSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSource.class);

    /**
     * Builds a new configuration source for specific local file from the file system.
     *
     * @param path the file path for this configuration source.
     */
    public FileSource(String path)
    {
        super(StringUtils.expandEnvironmentVariables(path));
    }

    @Override
    public T load(Mapper<InputStream, T> mapper)
    {
        LOGGER.info("Searching file: {}", super.source);

        try (InputStream inputStream = new FileInputStream(super.source))
        {
            return load(inputStream, mapper);
        }
        catch (IOException exception)
        {
            throw new ConfigurationSourceException(exception, "Unable to load file: %s", super.source);
        }
    }

    @Override
    protected T load(InputStream inputStream, Mapper<InputStream, T> mapper) throws IOException
    {
        LOGGER.info("Loading file {} with mapper: <{}>", super.source, mapper.getClass().getSimpleName());
        T mappedObject = mapper.apply(inputStream);

        LOGGER.info("File {} loaded successfully", super.source);
        return mappedObject;
    }

}